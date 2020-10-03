package it.scalachikoro.server.game

import akka.actor.{ActorRef, PoisonPill, Props, Terminated}
import it.scalachikoro.actors.MyActor
import it.scalachikoro.koro.game.{Game, GameState, Operation, Turn}
import it.scalachikoro.koro.players.{PlayerKoro, PlayerRef}
import it.scalachikoro.messages.GameMessages._
import it.scalachikoro.messages.LobbyMessages.Start

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt

/**
 * The Actor where all game logic is managed before send messages to clients.
 */
object GameActor {
  def props(playersNumber: Int): Props = Props(new GameActor(playersNumber))
}

class GameActor(playersNumber: Int) extends MyActor {
  var game: Game = _
  var turn: Turn[PlayerRef] = _

  override def receive: Receive = idle

  private[this] def idle: Receive = {
    case Start(players) =>
      require(players.size == playersNumber)
      this log f"Start a match with ${players.map(_.name)}. Waiting for their acceptance."
      turn = Turn(players)
      broadcastMessage(players.map(_.actorRef), GameInvitation())
      context.become(initializing(Seq.empty) orElse terminated)
    case a: Any => this log f"Received unknown message while in idle $a"
  }

  private[this] def initializing(readyPlayers: Seq[PlayerKoro]): Receive = {
    case Accept(name) =>
      this log f"Player $name is ready"
      val player = turn.all.find(_.name == name)
      if (player.isEmpty) {
        this log f"Player ${sender.path} with name $name didn't found."
        terminated
      } else {
        val updated = readyPlayers :+ PlayerKoro.init(player.get.id, player.get.name)
        if (updated.length == playersNumber) {
          this log "Start game"
          initializeGame(updated)
        } else {
          this log f"Initialized by ${updated.size}, waiting for ${playersNumber - updated.size}"
          context become(initializing(updated) orElse terminated)
        }
      }
    case Drop() =>
      context become terminated
    case a: Any => this log f"Received unknown message while in initializing $a"
  }

  private[this] def initializeGame(players: Seq[PlayerKoro]): Unit = {
    game = Game(players)
    this log f"Initialized game $game."

    // Notify to all players their Game States.
    turn.all
      .map(p => (p, players
        .find(k => k.id == p.id)
        .getOrElse(PlayerKoro.bank))
      ).foreach(p => p._1.actorRef ! UpdateState(self, GameState(game, p._2)))
    this log f"Sent state to all players."
    turn.actual.actorRef ! PlayerTurn
    this log f"Sent player turn to correct player."
    broadcastMessage(turn.all.filterNot(_ == turn.actual).map(_.actorRef), OpponentTurn(turn.actual))
    this log f"Sent opponent turn to correct players."
    context.become(rollTime(game, turn.actual) orElse terminated)
  }

  private[this] def rollTime(actual: Game, ref: PlayerRef): Receive = {
    case RollDice(n) if ref.actorRef == sender =>
      // First roll many dice as user want.
      val result = Game.roll(n)
      broadcastMessage(turn.all.map(_.actorRef), DiceRolled(result, ref))
      // Then apply the dice result to actual game state.
      val players = actual.applyDiceResult(result, ref.id)
      players.foreach {
        case Operation.Give(amount, to) =>
          // Give to other players their taxes.
          val player = turn.all.find(p => p.id equals to.id)
          withRef(player) { playerRef =>
            ref.actorRef ! Give(amount, to.id)
            playerRef.actorRef ! Receive(amount, ref.id)
          }
        case Operation.Receive(amount, from) =>
          // Receive incomes from the bank.
          ref.actorRef ! Receive(amount, from.id)
        case _ =>
      }
      // TODO: Return the new game state.
      context.become(acquireTime(actual, turn.actual) orElse terminated)
  }

  private [this] def acquireTime(actual: Game, ref: PlayerRef): Receive = {
    case Acquire(card) if ref.actorRef == sender =>
      val newState = actual acquireCard(card, ref.id)
      newState match {
        case Left(value) =>
          this log f"${ref.name} haven't acquired the card."
          ref.actorRef ! NotAcquired(value)
        case Right(value) =>
          this log f"${ref.name} have acquired the card."
          broadcastMessage(turn.all.map(_.actorRef), Acquired(card, ref))
          nextTurn(value)
      }
    case EndTurn() if ref.actorRef == sender => nextTurn(actual)
    case a: Any => this log f"Received unknown message while in rollTime $a"
  }

  private[this] def nextTurn(actual: Game): Unit = {
    turn.next.actorRef ! PlayerTurn
    broadcastMessage(turn.all filterNot(_ == turn.actual) map(_.actorRef), OpponentTurn(turn.actual))
    context become(rollTime(actual, turn.actual) orElse terminated)
  }

  private[this] def terminated: Receive = {
    case Terminated(ref) =>
      turn.all find(_.actorRef == ref) match {
        case Some(player) =>
          System.err println(f"Player ${player.name} terminated.")
          broadcastMessage(turn.all filterNot(_.actorRef == ref) map(_.actorRef), Drop()) // TODO: Change message.
          context.system.scheduler.scheduleOnce(20.second) {
            System.err println(f"Terminating game actor...")
            self ! PoisonPill
          }
        case _ => System.err println(f"Client with ${ref.path} not found.");
      }
    case a: Any => this log f"Received unknown message while in terminated $a"
  }

  private[this] def gameEnded(): Receive = {
    case _ => sender ! Drop() // TODO: Change this message.
  }

  private[this] def broadcastMessage(refs: Seq[ActorRef], message: Any): Unit = refs foreach(_ ! message)
}
