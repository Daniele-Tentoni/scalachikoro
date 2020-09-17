package it.scalachikoro.server.game

import akka.actor.{ActorRef, PoisonPill, Props, Terminated}
import it.scalachikoro.actors.MyActor
import it.scalachikoro.koro.game.{Game, Turn}
import it.scalachikoro.koro.players.{PlayerKoro, PlayerRef}
import it.scalachikoro.messages.GameMessages._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt

object GameActor {
  def props(playersNumber: Int): Props = Props(new GameActor(playersNumber))
}

class GameActor(playersNumber: Int) extends MyActor {
  var game: Game = _
  var turn: Turn[PlayerRef] = _

  override def receive: Receive = idle

  private def idle: Receive = {
    case Start(players) =>
      require(players.size == playersNumber)
      println(f"Start a match with ${players.map(_.name)}. Waiting for their acceptance.")
      turn = Turn(players)
      broadcastMessage(players.map(_.actorRef), GameFound())
      context.become(initializing(Seq.empty) orElse terminated)
  }

  private def initializing(readyPlayers: Seq[PlayerKoro]): Receive = {
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
  }

  private def initializeGame(players: Seq[PlayerKoro]): Unit = {
    game = Game(players)
    broadcastMessage(turn.all.map(_.actorRef), GameState(game))
    turn.get.actorRef ! PlayerTurn
    broadcastMessage(turn.all.filterNot(_ == turn.get).map(_.actorRef), OpponentTurn(turn.get))
    context.become(inTurn(game, turn.get) orElse terminated)
  }

  private def inTurn(value: Game, ref: PlayerRef): Receive = {
    case RollDice(n) if ref.actorRef == sender =>
      val newState = value.rollDice(n, ref.id)
      broadcastMessage(turn.all.map(_.actorRef), DiceRolled(newState._2))
      // TODO: Give and Receive moneys.
      context.become(inTurn(newState._1, turn.get) orElse terminated)
    case Acquire(card) if ref.actorRef == sender =>
      val newState = value.acquireCard(card, ref.id)
      // TODO: Check if player have acquired the card.
      broadcastMessage(turn.all.map(_.actorRef), Acquired(ref))
      // TODO: Go through only if player have acquired the card.
      nextTurn()
    case EndTurn() if ref.actorRef == sender => nextTurn()

  }

  private def nextTurn(): Unit = {
    turn.next.actorRef ! PlayerTurn
    broadcastMessage(turn.all.filterNot(_ == turn.get).map(_.actorRef), OpponentTurn(turn.get))
  }

  private def terminated: Receive = {
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
  }

  private def gameEnded(): Receive = {
    case _ => sender ! Drop() // TODO: Change this message.
  }

  private def broadcastMessage(refs: Seq[ActorRef], message: Any): Unit = refs.foreach(_ ! message)
}
