package it.scalachikoro.client.actors

import akka.actor.{ActorRef, Props}
import it.scalachikoro.actors.MyActor
import it.scalachikoro.client.controllers.GameEventListener
import it.scalachikoro.messages.GameMessages._

/**
 * The Actor where all game logic are received from the remote Game Actor.
 */
object GameActor {
  def props(name: String, listener: GameEventListener, ref: ActorRef): Props = Props(new GameActor(name, listener, ref))
}

class GameActor(name: String, listener: GameEventListener, ref: ActorRef) extends MyActor {
  def receive: Receive = {
    case UpdateState(ref, state) =>
      this log f"Received a new game state $state from $ref"
      listener.updateGameState(ref, state)
    case PlayerTurn() =>
      this log f"Received player turn"
      // TODO: Tell to player how many dices wanna roll.
      listener.roll(1)
    case OpponentTurn(player) =>
      this log f"Received another player turn"
    case DiceRolled(result, ref) =>
      this log f"Received a result of a dice roll"
      // TODO: Tell to player who have rolled dices.
      listener.rolled(result)
    case Receive(n) =>
      this log f"Received $n moneys"
      listener.receive(n)
    case Give(n, from) =>
      this log f"Need to give $n moneys to ${from.name}"
    // TODO: Notify the player to give moneys to another player.
    case Acquired(card, player) =>
      this log f"${player.name} have acquired $card"
      listener.acquired(player, card)
    case PlayerWon(player) =>
      this log f"${player.name} wom!"
      listener.playerWon(player)
    case a: Any => this log f"Received unknown message while in receive $a"
  }

  override def preStart(): Unit = {
    super.preStart()
    ref ! Ready(name) // TODO: Notify the remote server that all clients are ready to play.
    println("Sent ready")
  }
}
