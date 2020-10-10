package it.scalachikoro.client.actors

import akka.actor.{PoisonPill, Props, Terminated}
import it.scalachikoro.actors.MyActor
import it.scalachikoro.client.controllers.listeners.MainViewActorListener
import it.scalachikoro.koro.game.GameState
import it.scalachikoro.messages.GameMessages.{GameInvitation, UpdateState}
import it.scalachikoro.messages.LobbyMessages.{Hi, LeftQueue, Queued}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt

/**
 * The Actor where all communications with server are managed.
 */
object MainViewActor {
  def props(name: String, listener: MainViewActorListener): Props = Props(new MainViewActor(name, listener))
}

class MainViewActor(name: String, listener: MainViewActorListener) extends MyActor {
  def receive: Receive = lobby

  def lobby: Receive = {
    case Hi(remote) =>
      this log f"$remote said Hi!"
      context become (discovered orElse terminated)
      listener welcomed(name, sender)
  }

  def discovered: Receive = {
    case Queued(id, others) =>
      this log f"We are queue with id: $id."
      context become (queued orElse terminated)
      listener queued(id, name, others)
  }

  def queued: Receive = {
    case LeftQueue() =>
      this log f"We've left the queue."
      context become (discovered orElse terminated)
      listener queueLeft name

    case GameInvitation() =>
      this log "Game found"
      listener matchFound(name, sender)

    case UpdateState(remoteGame, state) =>
      state match {
        case GameState.BrokenGameState(message) => this log f"Received a broken game state with $message"
        case a: GameState.LocalGameState =>
          this log f"Update state message from ${remoteGame.path} with updated game state"
          listener gameStarted(remoteGame, a)
          context become (inactive orElse terminated)
        case a: Any => this log f"Received unknown state message with $a"
      }

    case a => this log f"Received unknown message $a."
  }

  def inactive: Receive = {
    case a => this log f"Received unknown message $a."
  }

  private[this] def terminated: Receive = {
    case Terminated(_) =>
      System.err.println(f"Actor ${self.path} terminated.")
      context.system.scheduler.scheduleOnce(20.second) {
        System.err.println(f"Terminating main view actor...")
        self ! PoisonPill
      }
    case _ => System.err.println(f"${sender.path} send an unknown message while ${self.path} is terminated.");
  }
}