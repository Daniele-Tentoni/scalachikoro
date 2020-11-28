package it.scalachikoro.server.lobby

import akka.actor.ActorRef
import it.scalachikoro.koro.players.PlayerRef
import org.scalatest.wordspec.AnyWordSpec

class LobbySpec extends AnyWordSpec {
  "A Lobby" when {
    var lobby: Lobby[PlayerRef] = PlayersLobby(Seq.empty[PlayerRef])
    "created" should {
      "be empty" in {
        assert(lobby.isEmpty)
      }
      "add a player" in {
        lobby = lobby + PlayerRef(ActorRef.noSender, "0", "1")
        assert(!lobby.isEmpty)
      }
      "don't remove a player if doesn't exist" in {
        lobby = lobby - "1"
        assert(!lobby.isEmpty)
      }
      "remove a player if exist" in {
        lobby = lobby - "0"
        assert(lobby.isEmpty)
      }
    }
    "reach maximum players" should {
      "don't produce a list if there are few players" in {
        lobby = lobby + PlayerRef(ActorRef.noSender, "0", "1")
        val (newLobby, player) = lobby.players(2)
        assert(!newLobby.isEmpty)
        assert(player.isEmpty)
      }
      "produce a list if there are few players" in {
        lobby = lobby + PlayerRef(ActorRef.noSender, "1", "2")
        val (newLobby, player) = lobby.players(2)
        assert(newLobby.isEmpty)
        assert(player.isDefined)
      }
    }
  }
}
