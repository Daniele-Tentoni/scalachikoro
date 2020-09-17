package it.scalachikoro.koro.players

import akka.actor.ActorRef

trait Referable {
  def actorRef: ActorRef
}
