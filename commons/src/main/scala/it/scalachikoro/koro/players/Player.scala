package it.scalachikoro.koro.players

import akka.actor.ActorRef

/**
 * A Player with a name and id. Used as father for many classes.
 */
abstract class Player() extends Identifiable {
  def name: String
}

/**
 * A Player with a reference to an Akka Actor.
 * @param actorRef Actor Reference.
 * @param id Identifier.
 * @param name Name.
 */
case class PlayerRef(override val actorRef: ActorRef, override val id: String, override val name: String) extends Player() with Referable