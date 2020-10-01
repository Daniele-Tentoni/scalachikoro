package it.scalachikoro.koro.players

import akka.actor.ActorRef

abstract class Player() extends Identifiable {
  def name: String
}

case class PlayerRef(override val actorRef: ActorRef, override val id: String, override val name: String) extends Player() with Referable