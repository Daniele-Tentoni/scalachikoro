package it.scalachikoro.game.players
import akka.actor.ActorRef

trait Player extends Identifiable {
  def name: String
}

class PlayerRef(override val name: String, override val actorRef: ActorRef, override val id: String) extends Player with Referable