package it.scalachikoro.server.game

import akka.actor.{ActorRef, PoisonPill, Props, Terminated}
import it.scalachikoro.actors.MyActor
import it.scalachikoro.koro.game.{Game, Turn}
import it.scalachikoro.koro.players.{PlayerKoro, PlayerRef}
import it.scalachikoro.messages.GameMessages._
import it.scalachikoro.messages.LobbyMessages.Start

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt

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
      println(f"Start a match with ${players.map(_.name)}. Waiting for their acceptance.")
      turn = Turn(players)
      broadcastMessage(players.map(_.actorRef), GameInvitation())
      context.become(initializing(Seq.empty) orElse terminated)
    case a: Any => this log f"Received unknown message while in idle $a"
  }

  private[this] def initializing(readyPlayers: Seq[PlayerKoro]): Receive = {
    case Accept(name) =>
      println(f"Player $name is ready")
      val player = turn.all.find(_.name == name)
      if (player.isEmpty) {
        println(f"Player ${sender.path} with name $name didn't found.")
        terminated
      } else {
        val updated = readyPlayers :+ PlayerKoro.init(player.get.id, player.get.name)
        if (updated.length == playersNumber) {
          println("Start game")
          initializeGame(updated)
        } else {
          println(f"Initialized by ${updated.size}, waiting for ${playersNumber - updated.size}")
          context.become(initializing(updated) orElse terminated)
        }
      }
    case Drop() =>
      context.become(terminated)
    case a: Any => this log f"Received unknown message while in initializing $a"
  }

  private[this] def initializeGame(players: Seq[PlayerKoro]): Unit = {
    game = Game(players)
    this log f"Initialized game $game."
    broadcastMessage(turn.all.map(_.actorRef), GameState(game))
    this log f"Sent state to all players."
    turn.get.actorRef ! PlayerTurn
    this log f"Sent player turn to correct player."
    broadcastMessage(turn.all.filterNot(_ == turn.get).map(_.actorRef), OpponentTurn(turn.get))
    this log f"Sent opponent turn to correct players."
    context.become(inTurn(game, turn.get) orElse terminated)
    this log f"Changed behaviour."
  }

  private[this] def inTurn(game: Game, ref: PlayerRef): Receive = {
    case RollDice(n) if ref.actorRef == sender =>
      val result = Game.roll(n)
      broadcastMessage(turn.all.map(_.actorRef), DiceRolled(result, ref))
      // TODO: Give and Receive moneys.
      val players = game.applyDiceResult(result, ref.id)
    // context.become(inTurn(game.copy(players = players), turn.get) orElse terminated)
    case Acquire(card) if ref.actorRef == sender =>
      val newState = game.acquireCard(card, ref.id)
      // TODO: Check if player have acquired the card.
      broadcastMessage(turn.all.map(_.actorRef), Acquired(card, ref))
      // TODO: Go through only if player have acquired the card.
      nextTurn()
    case EndTurn() if ref.actorRef == sender => nextTurn()
    case a: Any => this log f"Received unknown message while in inTurn $a"
  }

  private[this] def nextTurn(): Unit = {
    turn.next.actorRef ! PlayerTurn
    broadcastMessage(turn.all.filterNot(_ == turn.get).map(_.actorRef), OpponentTurn(turn.get))
  }

  private[this] def terminated: Receive = {
    case Terminated(ref) =>
      turn.all.find(_.actorRef == ref) match {
        case Some(player) =>
          System.err.println(f"Player ${player.name} terminated.")
          broadcastMessage(turn.all.filterNot(_.actorRef == ref).map(_.actorRef), Drop()) // TODO: Change message.
          context.system.scheduler.scheduleOnce(20.second) {
            System.err.println(f"Terminating game actor...")
            self ! PoisonPill
          }
        case _ => System.err.println(f"Client with ${ref.path} not found.");
      }
    case a: Any => this log f"Received unknown message while in terminated $a"
  }

  private[this] def gameEnded(): Receive = {
    case _ => sender ! Drop() // TODO: Change this message.
  }

  private[this] def broadcastMessage(refs: Seq[ActorRef], message: Any): Unit = refs.foreach(_ ! message)
}
