package it.scalachikoro.server.lobby

import java.util.UUID

import akka.actor.{Actor, ActorRef, Props}
import it.scalachikoro.commons.players.Player
import it.scalachikoro.messages.LobbyMessages.{Hi, Leave, LeftQueue, Queued, WannaQueue}
import it.scalachikoro.server.lobby.LobbyActor.PlayerRef

import scala.annotation.tailrec

object LobbyActor {
  def props(): Props = Props(new LobbyActor())

  trait Referable {
    val actorRef: ActorRef
  }

  trait IdGenerator {
    def generateId(): String
  }

  object MyIdGenerator extends IdGenerator {
    var ids = Set.empty[String]

    override def generateId(): String = UUID randomUUID() toString()

    @tailrec
    def generateUniqueId(): String = {
      val id = generateId()
      if (ids.contains(id))
        generateUniqueId()
      else {
        ids = ids + id
        id
      }
    }
  }

  case class PlayerRef(id: String, name: String, actorRef: ActorRef) extends Player with Referable

  def player(name: String, ref: ActorRef): PlayerRef = PlayerRef(MyIdGenerator generateUniqueId(), name, ref)
}

class LobbyActor extends Actor {
  var lobby: Lobby[PlayerRef] = PlayersLobby(Set.empty[PlayerRef])

  def receive: Receive = {
    case Hi(name) =>
      println(f"$name say Hi to us. Add to queue.")
      sender ! Hi("Server")

    case WannaQueue(name) =>
      println(f"$name wanna queue.")
      val p = LobbyActor.player(name, sender)
      lobby = lobby + p
      sender ! Queued(p.id)

    case Leave(id) =>
      println(f"Player $id wanna leave the queue.")
      lobby = lobby - id
      sender ! LeftQueue()

    case _ => println(f"${sender.path.name} send an unknown message.")
  }

  override def preStart(): Unit = super.preStart() // TODO: Start to fetch for matches.
}
