package it.scalachikoro.game.players

import akka.actor.ActorRef

trait Referable {
  def actorRef: ActorRef
}
