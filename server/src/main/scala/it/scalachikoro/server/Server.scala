package it.scalachikoro.server

import java.net.{DatagramSocket, InetAddress}

import akka.actor.{ActorSystem, Address, Deploy}
import akka.remote.RemoteScope
import akka.routing.FromConfig
import it.scalachikoro.constants.ActorConstants.{LobbyActorName, ServerActorSystemName}
import it.scalachikoro.server.lobby.LobbyActor

object Server extends App {
  val socket = new DatagramSocket()
  socket.connect(InetAddress.getByName("8.8.8.8"), 10002)
  val address = socket.getLocalAddress.getHostAddress
  println(f"Server is running on $address")
  val ip = new Address("akka", "sys", address, 1234)
  val server = ActorSystem(ServerActorSystemName)
  server.actorOf(LobbyActor.props().withRouter(FromConfig()).withDeploy(new Deploy(RemoteScope(ip))), name = LobbyActorName)
}
