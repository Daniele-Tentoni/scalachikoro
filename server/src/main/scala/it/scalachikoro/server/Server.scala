package it.scalachikoro.server

import akka.actor.ActorSystem
import akka.routing.FromConfig
import it.scalachikoro.constants.ActorConstants.LOBBY_ACTOR_NAME
import it.scalachikoro.server.lobby.LobbyActor

object Server extends App {
  val server = ActorSystem("Server")
  server.actorOf(LobbyActor.props().withRouter(FromConfig()), name = LOBBY_ACTOR_NAME)
}
