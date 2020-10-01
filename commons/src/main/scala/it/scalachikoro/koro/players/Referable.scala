package it.scalachikoro.koro.players

import akka.actor.ActorRef

/**
 * Define a referable object in the system with a remote actor.
 */
trait Referable {
  def actorRef: ActorRef
}
