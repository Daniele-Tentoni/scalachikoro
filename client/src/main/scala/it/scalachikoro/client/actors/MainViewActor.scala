package it.scalachikoro.client.actors

import akka.actor.{Actor, ActorRef, Props}
import it.scalachikoro.constants.ActorConstants.LOBBY_ACTOR_NAME
import it.scalachikoro.messages.GameMessages.MatchFound
import it.scalachikoro.messages.LobbyMessages.{Hi, Leave, LeftQueue, Queued, WannaQueue}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.util.{Failure, Success}

object MainViewActor {
  def props(name: String): Props = Props(new MainViewActor(name))
}

class MainViewActor(name: String) extends Actor {
  var server: Option[ActorRef] = Option.empty

  def receive: Receive = {
    case Hi(name) =>
      println(f"$name said Hi!")
      sender ! WannaQueue(this.name)

    case Queued(id) =>
      println(f"We are queue with id: $id.")
      print("What you wanna do now? ")
      withServerLobby {
        _ ! Leave(id)
      }

    case MatchFound() =>
      context.actorOf(GameActor.props(name, sender))

    case LeftQueue() =>
      println(f"We've left the queue.")

    case _ =>
      println("Received an unknown message.")
  }

  private def withServerLobby(f: ActorRef => Unit): Unit = server match {
    case Some(ref) => f(ref)
    case None => println("No server found.")
  }

  // This is the constructor section. Find where the server is located and send a first message.
  val path = f"akka.tcp://Server@127.0.0.1:47000/user/$LOBBY_ACTOR_NAME"
  context.system.actorSelection(path).resolveOne()(10.seconds) onComplete {
    case Success(ref: ActorRef) =>
      server = Option(ref)
      println(f"Located Server actor: $server.")
      withServerLobby {
        _ ! Hi(name)
      }

    case Failure(t) =>
      System.err.println(f"Failed to locate Server actor. Reason: $t")
      context.system.terminate()
  }
}