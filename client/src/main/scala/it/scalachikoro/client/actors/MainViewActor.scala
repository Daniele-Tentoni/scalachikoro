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
    case Hi(name, ref) =>
      context.become(discovered orElse terminated)
      println(f"$ref said Hi!")
      listener.welcomed(name)
  }

  def discovered(): Receive = {
    case Queued(id) =>
      println(f"We are queue with id: $id.")
      listener.queued(name)
      context.become(queued orElse terminated)
  }

  def queued: Receive = {
    case LeftQueue() =>
      println(f"We've left the queue.")
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
  }

  def inactive: Receive = {
    case _ => println("Received an unknown message.")
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