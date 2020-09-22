package it.scalachikoro.client.actors

import akka.actor.{ActorRef, Props}
import it.scalachikoro.actors.MyActor
import it.scalachikoro.messages.GameMessages._

object GameActor{
  def props(name: String, remoteGameRef: ActorRef): Props = Props(new GameActor(name, remoteGameRef))
}

class GameActor(name: String, remoteGameRef: ActorRef) extends MyActor {
  def receive: Receive = {
    case GameState(state) =>
    case PlayerTurn() =>
    case OpponentTurn(player) =>
    case DiceRolled(result, ref) =>
    case Receive(n) =>
    case Give(n, from) =>
    case Acquired(card, player) =>
    case PlayerWon(player) =>
    case a: Any => this log f"Received unknown message while in receive $a"
  }

  override def preStart(): Unit = {
    super.preStart()
    remoteGameRef ! Ready(name)
    println("Sended ready")
  }
}
