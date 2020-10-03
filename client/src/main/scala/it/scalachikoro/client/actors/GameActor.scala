package it.scalachikoro.client.actors

import akka.actor.{ActorRef, Props}
import it.scalachikoro.actors.MyActor
import it.scalachikoro.client.controllers.{GameEventListener, GamePanelListener}
import it.scalachikoro.messages.GameMessages._

/**
 * The Actor where all game logic are received from the remote Game Actor.
 */
object GameActor {
  def props(name: String, listener: GameEventListener with GamePanelListener, ref: ActorRef): Props = Props(new GameActor(name, listener, ref))
}

class GameActor(name: String, listener: GameEventListener, ref: ActorRef) extends MyActor {
  def receive: Receive = {
    case UpdateState(ref, state) =>
      this log f"Received a new game state $state from $ref"
      listener.updateGameState(state)
    case PlayerTurn() =>
      this log f"Received player turn"
      // TODO: Tell to player how many dices wanna roll.
    case OpponentTurn(player) =>
      this log f"Received another player turn"
    case DiceRolled(result, ref) =>
      this log f"Received a result of a dice roll"
      // TODO: Tell to player who have rolled dices.
      listener diceRolled result
    case Receive(n, from) =>
      this log f"Received $n moneys from $from"
      listener.receive(n, from)
    case Give(n, from) =>
      this log f"Need to give $n moneys to ${from}"
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
    ref ! Ready(self) // TODO: Notify the remote server that all clients are ready to play.
    println("Sent ready")
  }
}
