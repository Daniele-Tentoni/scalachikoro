package it.scalachikoro.server.lobby

import akka.actor.{ActorRef, Props, Terminated}
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
  val lobbyGlobal: Lobby[PlayerRef] = PlayersLobby(Seq.empty)

  def receive: Receive = looking(lobbyGlobal)

  private[this] def looking(lobby: Lobby[PlayerRef]): Receive = {
    // TODO: Ref isn't sender? Doesn't work with unit tests.
    case Connect(name, ref) => welcomePlayer(name, ref)

    // Those case may leave lobby different than they've found.
    case WannaQueue(name, ref) =>
      val biggerLobby = queuePlayer(name, ref, lobby)
      val smallerLobby = checkAndCreateGame(biggerLobby)
      context become (looking(smallerLobby) orElse terminated)

    case Leave(id) =>
      val smallerLobby = playerLeaveQueue(lobby, id)
      context become (looking(smallerLobby) orElse terminated)

    case a => this log f"${sender.path} send me an unknown message $a."
  }

  private[this] def terminated: Receive = {
    case Terminated(_) => this log "Lobby terminated"
    case _ => this error "Error in lobby actor terminated state"
  }

  private[this] def checkAndCreateGame(lobby: Lobby[PlayerRef]) = {
    this log f"Fetch for at least 2 players."
    val (smallerLobby, pickedPlayers) = lobby.players(2)
    pickedPlayers match {
      case Some(value) =>
        this log f"Found ${value map (_.name)} to start a match."
        generateGameActor(value)
      case _ =>
        this log f"No players found to start a match."
    }
    smallerLobby
  }

  private[this] def generateGameActor(players: Seq[PlayerRef]): Unit = {
    this log f"Generate a new Game Actor."
    val matchActor = context.actorOf(GameActor.props(players.size))
    matchActor ! Start(players)
    this log f"Start Game Actor with ${players.map(_.name)} players."
  }

  /**
   * Say welcome to a new player when he connect.
   * In the future, this return message can contains important news
   * or the state of the server.
   *
   * @param name name of the player.
   * @param ref  actor reference.
   */
  private[this] def welcomePlayer(name: String, ref: ActorRef): Unit = {
    this log f"${ref.path} with $name say Hi."
    ref ! Hi("Server")
  }

  /**
   * Add a player in the lobby.
   *
   * @param name  name of arrived player.
   * @param ref   player actor reference.
   * @param lobby actual lobby state.
   * @return new lobby state, with the player added.
   */
  private[this] def queuePlayer(name: String, ref: ActorRef, lobby: Lobby[PlayerRef]) = {
    this log f"${ref.path} with $name wanna queue."
    val p = LobbyActor.player(name, ref)
    this log f"There are ${lobby.items.size} players in the lobby."
    ref ! Queued(p.id, lobby.items.size)
    lobby + p
  }

  /**
   * Remove a player from the queue if it exist.
   *
   * @param lobby actual lobby state.
   * @param id    player identifier.
   * @return new lobby state, optionally without a player.
   */
  private[this] def playerLeaveQueue(lobby: Lobby[PlayerRef], id: String) = {
    this log s"$id wanna leave the queue"
    lobby.items find (_.id == id) match {
      case Some(ref) =>
        ref.actorRef ! LeftQueue()
        this log f"${ref.name} left the queue."
        lobby - id
      case None => lobby
    }
  }

  log(f"I'm listening")
}
