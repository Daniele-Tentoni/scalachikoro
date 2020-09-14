package it.scalachikoro.server.lobby

import akka.actor.{Actor, ActorRef, Props}
import it.scalachikoro.messages.LobbyMessages.{Hi, Leave, LeftQueue, Queued}
import it.scalachikoro.players.Player
import it.scalachikoro.server.lobby.LobbyActor.PlayerRef

import scala.collection.immutable.Queue

object LobbyActor {
  def props(): Props = Props(new LobbyActor())

  trait Referable{
    val actorRef: ActorRef
  }

  case class PlayerRef(name: String, actorRef: ActorRef) extends Player with Referable

  def player(name: String, ref: ActorRef): PlayerRef = PlayerRef(name, ref)
}

class LobbyActor extends Actor {
  var queue: Queue[PlayerRef] = Queue.empty[PlayerRef]
  def receive: Receive = {
    case Hi(name) =>
      println(f"$name say Hi to us. Add to queue.")
      queue = queue.enqueue(LobbyActor.player(name, sender))
      sender ! Queued(queue.size)

    case Leave() =>
      queue = queue.filterNot(f => f.actorRef != sender)
      sender ! LeftQueue()

    case _ => println(f"${sender.path.name} send an unknown message.")
  }
}
