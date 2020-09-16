package it.scalachikoro.client.actors

import akka.actor.{Actor, ActorRef, Props}
import it.scalachikoro.messages.GameMessages._

object GameActor{
  def props(name: String, serverRef: ActorRef): Props = Props(new GameActor(name, serverRef))
}

class GameActor(name: String, serverRef: ActorRef) extends Actor {
  def receive: Receive = {
    case GameState(state) =>
    case PlayerTurn() =>
    case OpponentTurn(player) =>
    case DiceRolled(result) =>
    case Receive(n) =>
    case Give(n, from) =>
    case Acquired(player) =>
    case PlayerWon(player) =>
    case _ => println(f"Received unknown message")
  }

  override def preStart(): Unit = {
    super.preStart()
    serverRef ! Ready(name)
    println("Sended ready")
  }
}
