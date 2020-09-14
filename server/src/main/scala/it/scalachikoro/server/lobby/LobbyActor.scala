package it.scalachikoro.server.lobby

import akka.actor.{Actor, ActorRef, Props}
import it.scalachikoro.messages.LobbyMessages.{Hi, Leave, LeftQueue, Queued}
import it.scalachikoro.players.Player
import it.scalachikoro.server.lobby.LobbyActor.PlayerRef

import scala.util.Random.nextInt

object LobbyActor {
  def props(): Props = Props(new LobbyActor())

  trait Referable {
    val actorRef: ActorRef
  }

  case class PlayerRef(id: String, name: String, actorRef: ActorRef) extends Player with Referable

  def player(name: String, ref: ActorRef): PlayerRef = PlayerRef(nextInt toString(), name, ref)
}

class LobbyActor extends Actor {
  var lobby: Lobby[PlayerRef] = PlayersLobby(Set.empty[PlayerRef])

  def receive: Receive = {
    case Hi(name) =>
      println(f"$name say Hi to us. Add to queue.")
      val p = LobbyActor.player(name, sender)
      lobby = lobby + p
      sender ! Queued(p.id)

    case Leave(id) =>
      println(f"Player $id wanna leave the queue.")
      lobby = lobby - id
      sender ! LeftQueue()

    case _ => println(f"${sender.path.name} send an unknown message.")
  }
}
