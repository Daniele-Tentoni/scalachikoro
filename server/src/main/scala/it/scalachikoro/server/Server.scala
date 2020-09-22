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
  val address = socket.getLocalAddress.getHostAddress
  val ip = new Address("akka.tcp", ServerActorSystemName, address, 47001)
  val server = ActorSystem(ServerActorSystemName, ConfigFactory.parseString(
    f"""
       |akka {
       |    logLevel = "DEBUG"
       |    actor {
       |        provider = remote
       |        warn-about-java-serializer-usage = false
       |
       |        debug {
       |            lifecycle = on
       |        }
       |
       |        deployment {
       |            /lobby {
       |                router = round-robin-pool
       |                nr-of-instances = 1 # More actors need more complex arch.
       |            }
       |        }
       |    }
       |    remote {
       |        enabled-transports = ["akka.remote.netty.tcp"]
       |        netty.tcp {
       |            hostname = "$address"
       |            port = 47000
       |        }
       |        log-sent-messages = on
       |        log-received-messages = on
       |    }
       |}
       |""".stripMargin))
  server.actorOf(LobbyActor.props().withRouter(FromConfig()), name = LobbyActorName)
}
