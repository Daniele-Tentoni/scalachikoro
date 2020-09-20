package it.scalachikoro.server

import java.net.{DatagramSocket, InetAddress}

import akka.actor.{ActorSystem, Address}
import akka.routing.FromConfig
import com.typesafe.config.ConfigFactory
import it.scalachikoro.constants.ActorConstants.{LobbyActorName, ServerActorSystemName}
import it.scalachikoro.server.lobby.LobbyActor

object Server extends App {
  val socket = new DatagramSocket()
  socket.connect(InetAddress.getByName("8.8.8.8"), 10002)
  val address = "192.168.1.50"// socket.getLocalAddress.getHostAddress
  println(f"Server is running on $address")
  val ip = new Address("akka.tcp", ServerActorSystemName, address, 47001)
  val server = ActorSystem(ServerActorSystemName, ConfigFactory.parseString(f"akka.remote.classic.netty.tcp.hostname=$address").withFallback(ConfigFactory.load()))
  server.actorOf(LobbyActor.props()
    .withRouter(FromConfig()), name = LobbyActorName)
}
