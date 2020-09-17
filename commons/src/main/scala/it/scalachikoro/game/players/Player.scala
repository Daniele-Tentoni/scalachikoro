package it.scalachikoro.game.players

import akka.actor.ActorRef

trait Player extends Identifiable {
  def name: String
}

class PlayerRef(override val actorRef: ActorRef, override val id: String, override val name: String) extends Player with Referable