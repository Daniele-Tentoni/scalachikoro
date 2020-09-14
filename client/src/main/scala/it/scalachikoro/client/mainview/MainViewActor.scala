package it.scalachikoro.client.mainview

import akka.actor.{Actor, ActorRef, Props}
import it.scalachikoro.messages.LobbyMessages.{Hi, Leave, LeftQueue, Queued}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.util.{Failure, Success}

object MainViewActor {
  def props(): Props = Props(new MainViewActor())
}

class MainViewActor extends Actor {
  var server: ActorRef = _

  def receive: Receive = {
    case Queued(pos) =>
      println(f"We are queue at position: $pos.")
      print("What you wanna do now? ")
      val action = askForAction()
      if(action == "leave" && server != null)
        server ! Leave()

    case LeftQueue() =>
      println(f"We've left the queue.")

    case _ =>
      println("Received an unknown message.")
  }

  def askForAction(): String = scala.io.StdIn.readLine()

  // This is the constructor section. Find where the server is located and send a first message.
  context.system.actorSelection("akka.tcp://Server@127.0.0.1:47000/user/server").resolveOne()(10.seconds) onComplete {
    case Success(ref: ActorRef) =>
      server = ref
      println(f"Located Server actor: $server.")
      print("Write your name: ")
      val name = askForAction()
      server ! Hi(name)
    case Failure(t) =>
      System.err.println(f"Failed to locate Server actor. Reason: $t")
      context.system.terminate()
  }
}
