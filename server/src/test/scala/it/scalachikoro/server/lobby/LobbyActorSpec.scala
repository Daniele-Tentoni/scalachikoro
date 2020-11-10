package it.scalachikoro.server.lobby

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit, TestProbe}
import com.typesafe.config.ConfigFactory
import it.scalachikoro.Constants
import it.scalachikoro.messages.LobbyMessages._
import org.scalamock.scalatest.MockFactory
import org.scalatest.BeforeAndAfterAll
import org.scalatest.wordspec.AnyWordSpecLike

class LobbyActorSpec extends TestKit(ActorSystem(Constants.test, ConfigFactory.load(Constants.test)))
  with ImplicitSender
  with AnyWordSpecLike
  with BeforeAndAfterAll
  with MockFactory {
  private[this] val NUMBER_OF_PLAYERS = 4
  private[this] val CLIENT_NAME = "Test Client"
  private[this] val SERVER_NAME = "Server"

  override protected def afterAll(): Unit = TestKit.shutdownActorSystem(system)

  "A lobby actor" should {
    "successfully welcome a player" in {
      val lobbyActor = system.actorOf(LobbyActor.props())
      val client = TestProbe()
      lobbyActor ! Connect(CLIENT_NAME, client.ref)
      val name = client.expectMsgPF() { case Hi(name) => name }
      assertResult(SERVER_NAME)(name)
    }

    "accept a queue request" in {
      val actor = system.actorOf(LobbyActor.props())
      val client = TestProbe()
      actor ! Connect("Test client", client.ref)
      val name = client.expectMsgPF() { case Hi(name) => name }
      assertResult(SERVER_NAME)(name)
      actor ! WannaQueue(CLIENT_NAME, client.ref)
      val id = client.expectMsgPF() {
        case Queued(id, _) => id
      }

      actor ! Leave(id)
      client.expectMsgType[LeftQueue]
    }

    "send an unknown message" in {

    }
  }
}