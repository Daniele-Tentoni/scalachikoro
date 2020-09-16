package it.scalachikoro.client.actors

import akka.actor.{Actor, ActorRef, Props}
import it.scalachikoro.client.controllers.MainViewActorListener
import it.scalachikoro.messages.GameMessages.{MatchFound, Start}
import it.scalachikoro.messages.LobbyMessages.{Hi, LeftQueue, Queued, WannaQueue}

object MainViewActor {
  def props(name: String, listener: MainViewActorListener): Props = Props(new MainViewActor(name, listener))
}

class MainViewActor(name: String, listener: MainViewActorListener) extends Actor {
  var server: Option[ActorRef] = Option.empty

  def receive: Receive = {
    case Hi(name, ref) =>
      server = Some(ref)
      println(f"$ref said Hi!")
      withServerLobby{serverRef => serverRef ! WannaQueue(name)}
      // listener.welcomed(name)

    case Queued(id) =>
      println(f"We are queue with id: $id.")
      listener.queued(name)

    case LeftQueue() =>
      println(f"We've left the queue.")
      listener.left(name)

    case MatchFound() =>
      println("Match found")
      listener.matchFound(name)

    case Start(players) =>
      println(f"Start message received with $players")
      context.actorOf(GameActor.props(name, sender))
    // TODO: Generate new view.

    case _ =>
      println("Received an unknown message.")
  }

  private def withServerLobby(f: ActorRef => Unit): Unit = server match {
    case Some(ref) => f(ref)
    case None =>
      System.err.println(
        f"""***********************************************
           |No server found.""")
      context.system.terminate()
  }
}