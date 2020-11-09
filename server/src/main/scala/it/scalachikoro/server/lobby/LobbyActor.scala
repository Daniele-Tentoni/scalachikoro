package it.scalachikoro.server.lobby

import akka.actor.{ActorRef, Props}
import it.scalachikoro.actors.MyActor
import it.scalachikoro.koro.players.PlayerRef
import it.scalachikoro.messages.LobbyMessages._
import it.scalachikoro.server.MyIdGenerator
import it.scalachikoro.server.game.GameActor

/**
 * The Actor where all play requests from Clients are received.
 */
object LobbyActor {
  def props(): Props = Props(new LobbyActor())

  def player(name: String, ref: ActorRef): PlayerRef = PlayerRef(ref, MyIdGenerator generateUniqueId(), name)
}

class LobbyActor extends MyActor {
  var lobby: Lobby[PlayerRef] = PlayersLobby(Seq.empty)

  def receive: Receive = {
    // TODO: Ref isn't sender? Dosen't work with unit tests.
    case Connect(name, ref) =>
      this log f"${ref.path} with $name say Hi."
      ref ! Hi("Server")

    case WannaQueue(name, ref) =>
      this log f"${ref.path} with $name wanna queue."
      val p = LobbyActor.player(name, ref)
      lobby = lobby + p
      this log f"There are ${lobby.items.size} players in the lobby."
      ref ! Queued(p.id, lobby.items.size)
      checkAndCreateGame()

    case Leave(id) =>
      this log s"$id wanna leave the queue"
      withRef(lobby.items find (_.id == id)) { ref =>
        lobby = lobby - id
        this log f"${ref.name} left the queue."
        ref.actorRef ! LeftQueue()
      }

    case a => this log f"${sender.path} send me an unknown message $a."
  }

  private[this] def checkAndCreateGame(): Unit = {
    this log f"Fetch for at least 2 players."
    val (_, pickedPlayers) = lobby.players(2)
    pickedPlayers match {
      case Some(value) =>
        this log f"Found ${value map (_.name)} to start a match."
        generateGameActor(value)
      case _ =>
        this log f"No players found to start a match."
    }
  }

  private[this] def generateGameActor(players: Seq[PlayerRef]): Unit = {
    this log f"Generate a new Game Actor."
    val matchActor = context.actorOf(GameActor.props(players.size))
    matchActor ! Start(players)
    this log f"Start Game Actor with ${players.map(_.name)} players."
  }

  log(f"I'm listening")
}
