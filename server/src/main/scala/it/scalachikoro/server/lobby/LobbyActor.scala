package it.scalachikoro.server.lobby

import akka.actor.{Actor, ActorRef, Props}
import it.scalachikoro.game.players.PlayerRef
import it.scalachikoro.messages.GameMessages.Start
import it.scalachikoro.messages.LobbyMessages._
import it.scalachikoro.server.MyIdGenerator
import it.scalachikoro.server.`match`.MatchActor

object LobbyActor {
  def props(): Props = Props(new LobbyActor())

  def player(name: String, ref: ActorRef): PlayerRef = new PlayerRef(MyIdGenerator generateUniqueId(), ref, name)
}

class LobbyActor extends Actor {
  var lobby: Lobby[PlayerRef] = PlayersLobby(Seq.empty)

  def receive: Receive = {
    case Hi(name) =>
      println(f"$name say Hi to us.")
      sender ! Hi("Server")

    case WannaQueue(name) =>
      println(f"$name wanna queue.")
      val p = LobbyActor.player(name, sender)
      lobby = lobby + p
      sender ! Queued(p.id)
      checkAndCreateGame()

    case Leave(id) =>
      println(f"Player $id wanna leave the queue.")
      lobby = lobby - id
      sender ! LeftQueue()

    case _ => println(f"${sender.path.name} send an unknown message.")
  }

  override def preStart(): Unit = super.preStart() // TODO: Start to fetch for matches.

  private def checkAndCreateGame(): Unit = {
    val p = lobby.getItems(4)
    p._2 match {
      case Some(value) => generateMatchActor(value)
      case _ =>
    }
  }

  private def generateMatchActor(players: Seq[PlayerRef]): Unit = {
    val matchActor = context.actorOf(MatchActor.props(players.size))
    matchActor ! Start(players)
  }
}
