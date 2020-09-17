package it.scalachikoro.server.lobby

import akka.actor.{ActorRef, Props}
import it.scalachikoro.actors.MyActor
import it.scalachikoro.game.players.PlayerRef
import it.scalachikoro.messages.GameMessages.Start
import it.scalachikoro.messages.LobbyMessages._
import it.scalachikoro.server.MyIdGenerator
import it.scalachikoro.server.`match`.MatchActor

object LobbyActor {
  def props(): Props = Props(new LobbyActor())

  def player(name: String, ref: ActorRef): PlayerRef = new PlayerRef(ref, MyIdGenerator generateUniqueId(), name)
}

class LobbyActor extends MyActor {
  var lobby: Lobby[PlayerRef] = PlayersLobby(Seq.empty)

  def receive: Receive = {
    case Hi(name, ref) =>
      log(f"${ref.path} with $name say Hi.")
      ref ! Hi("Server", self)

    case WannaQueue(name, ref) =>
      log(f"${ref.path} with $name wanna queue.")
      val p = LobbyActor.player(name, ref)
      lobby = lobby + p
      ref ! Queued(p.id)
      checkAndCreateGame()

    case Leave(id) =>
      log(f"${sender.path} with $id wanna leave the queue.")
      val leaver = lobby.items.find(_.id == id)
      lobby = lobby - id
      withRef(leaver) {
        _.actorRef ! LeftQueue()
      }

    case _ => log(f"${sender.path} send me an unknown message.")
  }

  private def checkAndCreateGame(): Unit = {
    val p = lobby.getItems(1)
    p._2 match {
      case Some(value) => generateMatchActor(value)
      case _ =>
    }
  }

  private def generateMatchActor(players: Seq[PlayerRef]): Unit = {
    val matchActor = context.actorOf(MatchActor.props(players.size))
    matchActor ! Start(players)
    log(f"Start Match Actor with ${players.map(_.name)} players.")
  }

  log(f"I'm listening")
}
