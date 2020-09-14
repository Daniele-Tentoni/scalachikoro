package it.scalachikoro.server.lobby

import akka.actor.ActorSystem
import akka.routing.FromConfig

object Server extends App {
  val server = ActorSystem("Server")
  server.actorOf(LobbyActor.props().withRouter(FromConfig()), name = "lobby")
}
