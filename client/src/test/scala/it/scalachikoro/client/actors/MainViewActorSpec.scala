package it.scalachikoro.client.actors

import akka.actor.ActorRef.noSender
import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestActorRef, TestKit}
import com.typesafe.config.ConfigFactory
import it.scalachikoro.client.controllers.MainViewActorListener
import it.scalachikoro.messages.GameMessages.{GameInvitation, Start}
import it.scalachikoro.messages.LobbyMessages.{Hi, LeftQueue, Queued}
import org.scalamock.scalatest.MockFactory
import org.scalatest.BeforeAndAfterAll
import org.scalatest.wordspec.AnyWordSpecLike

class MainViewActorSpec extends TestKit(ActorSystem("test", ConfigFactory.load("test")))
  with ImplicitSender
  with AnyWordSpecLike
  with BeforeAndAfterAll
  with MockFactory {
  override protected def afterAll(): Unit = TestKit.shutdownActorSystem(system)

  private def provideListener = stub[MainViewActorListener]

  val name = "Test Actor"

  private def provideActorRef(listener: MainViewActorListener) = TestActorRef[MainViewActor](MainViewActor.props(name, listener))

  "A MainViewActor" should {
    val mockListener = provideListener
    val mockActor = provideActorRef(mockListener)

    "notify the listener on game state updated" in {
      mockActor ! Hi(name)
      mockListener.welcomed _ verify (name, noSender) once()
    }

    "notify the listener on queue done" in {
      val id = "1"
      mockActor ! Queued(id, 1)
      pending
      mockListener.queued _ verify (id, name, 1) once()
    }

    "notify the listener on queue left" in {
      mockActor ! LeftQueue()
      mockListener.queueLeft _ verify * once()
    }

    "notify the listener on Game Found" in {
      mockActor ! GameInvitation()
      mockListener.matchFound _ verify(*, *) once()
    }

    "notify the listener on Game Start" in {
      mockActor ! Start(Seq.empty)
      pending
    }
  }
}
