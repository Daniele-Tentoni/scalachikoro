package it.scalachikoro.server

import java.net.InetAddress

import akka.actor.ActorSystem
import akka.routing.FromConfig
import it.scalachikoro.constants.ActorConstants.LOBBY_ACTOR_NAME
import it.scalachikoro.server.lobby.LobbyActor

object Server extends App {
  val address: InetAddress = InetAddress.getLocalHost
  println(f"Server is running on ${address.getHostAddress}")
  val server = ActorSystem("Server")
  server.actorOf(LobbyActor.props().withRouter(FromConfig()), name = LOBBY_ACTOR_NAME)
}
