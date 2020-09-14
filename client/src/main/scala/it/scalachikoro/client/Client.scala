package it.scalachikoro.client

import akka.actor.ActorSystem
import it.scalachikoro.client.mainview.MainViewActor

object Client extends App {
  val client = ActorSystem("Client")
  client.actorOf(MainViewActor.props())
}
