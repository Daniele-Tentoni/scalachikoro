package it.scalachikoro.server.game

import akka.actor.{ActorRef, PoisonPill, Props, Terminated}
import it.scalachikoro.actors.MyActor
import it.scalachikoro.koro.game.{Game, GameState}
import it.scalachikoro.koro.players.PlayerRef
import it.scalachikoro.messages.GameMessages._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt

/**
 * A fake actor used to test all player features.
 */
object IAGameActor {
  def props(server: ActorRef): Props = Props(new IAGameActor(server))
}

class IAGameActor(server: ActorRef) extends MyActor {
  def receive: Receive = {
    case UpdateState(ref, state) =>
      this log f"Received a new state $state from $ref"
      context become (gameStarted(state) orElse terminated)
  }

  def gameStarted(game: GameState): Receive = {
    case PlayerTurn() =>
      this log f"Must start the turn"
      val result = Game.roll(1) // TODO: Change number of dices dynamically.
      this log f"Dice rolled for $result"
      server ! DiceRolled(result, PlayerRef(self, "", ""))

    case OpponentTurn(player) =>
      this log f"It's ${player.name} turn"
      context.become(gameStarted(game) orElse terminated)

    case DiceRolled(result, ref) =>
      this log f"Dice rolled message with $result by $ref"

    case Receive(n) =>
      this log f"Received $n moneys."

    case Give(n, from) =>
      this log f"Must give $n moneys to $from"

    case Acquired(card, player) =>
      this log f"${card.name} acquired from ${player.name}"

    case PlayerWon(player) =>
      this log f"${player.name} won."
      context become terminated

    case _ => println(f"Received unknown message")
  }

  def terminated: Receive = {
    case Terminated(_) =>
      System.err.println(f"Actor ${self.path} terminated.")
      context.system.scheduler.scheduleOnce(20.second) {
        System.err.println(f"Terminating main view actor...")
        self ! PoisonPill
      }
    case _ => println(f"Received unknown message")
  }
}
