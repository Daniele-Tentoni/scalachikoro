package it.scalachikoro.client.controllers

import akka.actor.{ActorRef, ActorSystem}
import it.scalachikoro.client.actors.MainViewActor
import it.scalachikoro.client.views.stages.StartupStage
import it.scalachikoro.client.views.utils.KoroAlert
import it.scalachikoro.constants.ActorConstants.LobbyActorName
import it.scalachikoro.messages.GameMessages.{Accept, Drop}
import it.scalachikoro.messages.LobbyMessages.{Hi, Leave, WannaQueue}
import scalafx.application.{JFXApp, Platform}
import scalafx.scene.control.ButtonType

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.util.{Failure, Success}

trait MainViewActorListener {
  /**
   * Say Hi to the server actor.
   *
   * @param name Name to present to the server.
   */
  def hi(name: String)

  /**
   * Return the response of an Hi message.
   *
   * @param name Name received.
   */
  def welcomed(name: String)

  /**
   * Say to server to queue the player.
   *
   * @param name Name of the player to queue.
   */
  def queue(name: String)

  /**
   * Return the response of the queue message.
   *
   * @param name Name of player queued.
   */
  def queued(name: String)

  /**
   * Say to server to leave the queue.
   */
  def leaveQueue()

  /**
   * Return the response of a queue left.
   */
  def queueLeft(name: String)

  /**
   * Return the response of a match found.
   */
  def matchFound(name: String, gameRef: ActorRef)

  /**
   * Say to server the response to the game call.
   *
   * @param response Response to the game call.
   */
  def inviteAccepted(name: String, response: Boolean, gameRef: ActorRef)

  // TODO: Add a comment to this.
  def gameStarted()
}

// TODO: Add a comment to this.
class StartupController(system: ActorSystem, app: JFXApp) extends Controller with MainViewActorListener {
  private var startUpStage: StartupStage = _
  var serverLobbyRef: Option[ActorRef] = None
  var startupActor: ActorRef = _
  val gameController = new GameController(system, app)

  /**
   * @inheritdoc
   */
  override def start(): Unit = Platform.runLater {
    startUpStage = StartupStage(this)
    app.stage = startUpStage
  }

  /**
   * @inheritdoc
   */
  override def stop(): Unit = {
    gameController.stop()
    println(f"Startup Controller stopped.")
  }

  override def hi(name: String): Unit = {
    println("Starting main view actor.")
    // This is the constructor section. Find where the server is located and send a first message.
    startupActor = system.actorOf(MainViewActor.props(name, this))
    val path = f"akka.tcp://Server@127.0.0.1:47000/user/$LobbyActorName"
    system.actorSelection(path).resolveOne()(10.seconds) onComplete {
      case Success(ref: ActorRef) =>
        serverLobbyRef = Option(ref)
        println(f"Located Server actor: $serverLobbyRef.")
        withServerLobbyRef { ref => ref ! Hi(name, startupActor) }

      case Failure(t) =>
        System.err.println(
          f"""***********************************************
             |Failed to locate Server actor.
             |Reason: $t""".stripMargin)
        system.terminate()
    }
  }

  override def welcomed(name: String): Unit = Platform.runLater {
    KoroAlert.info("Welcome", "You are welcome") showAndWait()
  }

  override def queue(name: String): Unit = withServerLobbyRef { serverRef => serverRef ! WannaQueue(name, startupActor) }

  override def queued(name: String): Unit = Platform.runLater {
    KoroAlert.info("Queued", "You are now in queue") showAndWait()
  }

  /**
   * @inheritdoc
   */
  override def leaveQueue(): Unit = withServerLobbyRef { ref => ref ! Leave("1") }

  override def queueLeft(name: String): Unit = Platform.runLater {
    KoroAlert.info("Sad", "I'm sad.") showAndWait()
  }

  override def matchFound(name: String, gameRef: ActorRef): Unit = Platform.runLater {
    val alert = KoroAlert.confirmation("Wanna join", "You really wanna join") showAndWait()

    // React to user's selection.
    inviteAccepted(name, alert.contains(ButtonType.OK), gameRef)
  }

  override def inviteAccepted(name: String, response: Boolean, gameRef: ActorRef): Unit = withServerLobbyRef { ref =>
    if (response)
      gameRef ! Accept(name)
    else
      gameRef ! Drop()
  }

  private def withServerLobbyRef(f: ActorRef => Unit): Unit = {
    this.serverLobbyRef match {
      case Some(ref) => f(ref)
      case None => println(f"No server actor.")
    }
  }

  override def gameStarted(): Unit = {
    gameController.start()
    println("GameController started.")
  }
}
