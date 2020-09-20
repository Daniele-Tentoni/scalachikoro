package it.scalachikoro.client.actors

import akka.actor.{PoisonPill, Props, Terminated}
import it.scalachikoro.actors.MyActor
import it.scalachikoro.client.controllers.MainViewActorListener
import it.scalachikoro.messages.GameMessages.{Drop, GameFound, Start}
import it.scalachikoro.messages.LobbyMessages.{Hi, LeftQueue, Queued}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt

object MainViewActor {
  def props(name: String, listener: MainViewActorListener): Props = Props(new MainViewActor(name, listener))
}

class MainViewActor(name: String, listener: MainViewActorListener) extends MyActor {
  def receive: Receive = lobby

  def lobby: Receive = {
    case Hi(name) =>
      context.become(discovered orElse terminated)
      log(f"$name said Hi!")
      listener.welcomed(name)
  }

  def discovered(): Receive = {
    case Queued(id, others) =>
      println(f"We are queue with id: $id.")
      context.become(queued orElse terminated)
      listener.queued(id, name, others)
  }

  def queued: Receive = {
    case LeftQueue() =>
      this log f"We've left the queue."
      listener.queueLeft(name)

    case GameFound() =>
      println("Game found")
      listener.matchFound(name, sender)

    case Start(players) =>
      println(f"Start message received with $players")
      context.become(inactive)
    // TODO: Generate new view.

    case Drop() =>
    // TODO: Drop the game and return to not in queue.

    case _ => this log f"Received unknown message"
  }

  def inactive: Receive = {
    case _ => this log "Received an unknown message."
  }

  private def terminated: Receive = {
    case Terminated(_) =>
      System.err.println(f"Actor ${self.path} terminated.")
      context.system.scheduler.scheduleOnce(20.second) {
        System.err.println(f"Terminating main view actor...")
        self ! PoisonPill
      }
    case _ => System.err.println(f"${sender.path} send an unknown message while ${self.path} is terminated.");
  }
}