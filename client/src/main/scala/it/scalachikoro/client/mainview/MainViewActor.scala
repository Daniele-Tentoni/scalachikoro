package it.scalachikoro.client.mainview

import akka.actor.{Actor, ActorRef, Props}
import it.scalachikoro.constants.ActorConstants.LOBBY_ACTOR_NAME
import it.scalachikoro.messages.LobbyMessages.{Hi, Leave, LeftQueue, Queued}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.util.{Failure, Success}

object MainViewActor {
  def props(): Props = Props(new MainViewActor())
}

class MainViewActor extends Actor {
  var server: Option[ActorRef] = Option.empty

  def receive: Receive = {
    case Hi(name) =>
      println(f"$name said Hi!")
      
    case Queued(id) =>
      println(f"We are queue with id: $id.")
      print("What you wanna do now? ")
      val action = askForAction()
      if (action == "leave")
        withServerLobby {
          _ ! Leave(id)
        }

    case LeftQueue() =>
      println(f"We've left the queue.")

    case _ =>
      println("Received an unknown message.")
  }

  def askForAction(): String = scala.io.StdIn.readLine()

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
      print("Write your name: ")
      val name = askForAction()
      withServerLobby {
        _ ! Hi(name)
      }

    case Failure(t) =>
      System.err.println(f"Failed to locate Server actor. Reason: $t")
      context.system.terminate()
  }
}
