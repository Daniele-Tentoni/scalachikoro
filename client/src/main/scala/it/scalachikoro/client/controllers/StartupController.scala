package it.scalachikoro.client.controllers

import akka.actor.ActorRef.noSender
import akka.actor.{ActorRef, ActorSystem}
import it.scalachikoro.client.actors.MainViewActor
import it.scalachikoro.client.views.stages.StartupStage
import it.scalachikoro.client.views.utils.KoroAlert
import it.scalachikoro.constants.ActorConstants.{LobbyActorName, ServerActorSystemName}
import it.scalachikoro.koro.players.PlayerRef
import it.scalachikoro.messages.GameMessages.{Accept, Drop}
import it.scalachikoro.messages.LobbyMessages.{Connect, Leave, WannaQueue}
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
  def connect(name: String, server: String, port: String)

  /**
   * Return the response of an Hi message.
   *
   * @param name   Name received.
   * @param server Get the correct server reference.
   */
  def welcomed(name: String, server: ActorRef)

  /**
   * Say to server to queue the player.
   *
   * @param name Name of the player to queue.
   */
  def queue(name: String)

  /**
   * Return the response of the queue message.
   *
   * @param id     Id of player given by the system.
   * @param name   Name of player queued.
   * @param others Number of other players.
   */
  def queued(id: String, name: String, others: Int)

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

  /**
   * The Server says that a Game is started.
   */
  def gameStarted()
}

class StartupController(system: ActorSystem, app: JFXApp) extends Controller with MainViewActorListener {
  private[this] val startUpStage = StartupStage(this)
  var serverLobbyRef: Option[ActorRef] = None
  var player: PlayerRef = PlayerRef(noSender, "", "")
  val gameController = new GameController(system, app)

  /**
   * @inheritdoc
   */
  override def start(): Unit = Platform.runLater {
    app.stage = startUpStage
  }

  /**
   * @inheritdoc
   */
  override def stop(): Unit = {
    gameController.stop()
    println(f"Startup Controller stopped.")
  }

  /**
   * @inheritdoc
   */
  override def connect(name: String, server: String, port: String): Unit = {
    println("Starting main view actor.")
    // This is the constructor section. Find where the server is located and send a first message.
    val actor = system.actorOf(MainViewActor.props(name, this))
    player = player.copy(actorRef = actor)
    val path = f"akka.tcp://$ServerActorSystemName@$server:$port/user/$LobbyActorName"
    system.actorSelection(path).resolveOne()(10.seconds) onComplete {
      case Success(ref: ActorRef) =>
        serverLobbyRef = Option(ref)
        println(f"Located Server actor: $serverLobbyRef.")
        withServerLobbyRef { ref => ref ! Connect(name, actor) }

      case Failure(t) =>
        System.err.println(
          f"""***********************************************
             |Failed to locate Server actor.
             |Reason: $t""".stripMargin)
        system.terminate() // TODO: Why the system is terminating?
    }
  }

  /**
   * @inheritdoc
   */
  override def welcomed(name: String, server: ActorRef): Unit = Platform runLater {
    KoroAlert info("Welcome", "You are welcome") showAndWait()
    player = player.copy(name = name)
    serverLobbyRef = Some(server)
    startUpStage goToQueueScene player.name
  }

  /**
   * @inheritdoc
   */
  override def queue(name: String): Unit = withServerLobbyRef {
    _ ! WannaQueue(name, player.actorRef)
  }

  /**
   * @inheritdoc
   */
  override def queued(id: String, name: String, others: Int): Unit = Platform.runLater {
    player = player.copy(id = id)
    startUpStage queued others
    // KoroAlert.info("Queued", "You are now in queue") showAndWait()
  }

  /**
   * @inheritdoc
   */
  override def leaveQueue(): Unit = withServerLobbyRef {
    _ ! Leave(player.id)
  }

  /**
   * @inheritdoc
   */
  override def queueLeft(name: String): Unit = Platform runLater {
    startUpStage enqueued()
  }

  /**
   * @inheritdoc
   */
  override def matchFound(name: String, gameRef: ActorRef): Unit = Platform runLater {
    val alert = KoroAlert.confirmation("Wanna join", "You really wanna join") showAndWait()

    // React to user's selection.
    inviteAccepted(name, alert contains ButtonType.OK, gameRef)
  }

  /**
   * @inheritdoc
   */
  override def inviteAccepted(name: String, response: Boolean, gameRef: ActorRef): Unit =
    if (response)
      gameRef ! Accept(name)
    else
      gameRef ! Drop()

  /**
   * @inheritdoc
   */
  override def gameStarted(): Unit = {
    gameController start()
    println("GameController started.")
  }

  /**
   * Invoke a function only if have some actor reference.
   *
   * @param f Function to invoke.
   */
  private[this] def withServerLobbyRef(f: ActorRef => Unit): Unit = {
    this.serverLobbyRef match {
      case Some(ref) => f(ref)
      case None => println(f"No server actor.")
    }
  }
}
