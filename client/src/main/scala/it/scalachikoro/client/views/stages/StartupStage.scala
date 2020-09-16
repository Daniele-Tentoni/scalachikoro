package it.scalachikoro.client.views.stages

import akka.actor.{ActorRef, ActorSystem}
import it.scalachikoro.client.actors.MainViewActor
import it.scalachikoro.client.views.KoroAlert
import it.scalachikoro.messages.GameMessages.Ready
import it.scalachikoro.messages.LobbyMessages.{Hi, WannaQueue}
import scalafx.beans.property.DoubleProperty
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.Scene
import scalafx.scene.control.{Button, ButtonType, Label, TextField}
import scalafx.scene.layout.{BorderPane, StackPane, VBox}

class StartupScene(listener: StartupListener) extends Scene() {
  // TODO: Add a background.

  val usernameLabel: Label = Label("Username")
  val usernameField: TextField = new TextField()
  val btnHi: Button = new Button("Hi")
  btnHi.onAction = _ => submit()

  val btnQueue = new Button("Queue")
  btnQueue.onAction = _ => enqueue()

  val center: VBox = new VBox()
  center.alignment = Pos.Center
  center.spacing = 10
  center.setMaxWidth(400)
  center.getChildren.addAll(usernameLabel, usernameField, btnHi)

  val mainContent: BorderPane = new BorderPane()
  mainContent.prefWidth <== DoubleProperty(800)
  mainContent.maxHeight <== DoubleProperty(640)
  mainContent.setPadding(Insets(5))
  mainContent.center = center

  val rootContent = new StackPane()
  rootContent.getChildren.addAll(mainContent)
  root = rootContent
  content = Seq(mainContent)

  private def submit(): Unit = {
    val username: String = usernameField.getText

    if (!username.isEmpty) {
      listener.hi(username)
    } else {
      KoroAlert info("Input error", "Some input error") showAndWait()
    }
  }

  private def enqueue(): Unit = {
    val username: String = usernameField.getText

    if (!username.isEmpty) {
      listener.queue(username)
    } else {
      KoroAlert info("Input error", "Some input error") showAndWait()
    }
  }

  private def acceptance(): Unit = {
    val alert = KoroAlert.confirmation("Wanna join", "You really wanna join")
    val result = alert.showAndWait()

    // React to user's selection.
    result match {
      case Some(ButtonType.OK) => listener.accept(true)
      case _                   => listener.accept(false)
    }
  }
}

trait StartupStage extends BaseStage

trait StartupListener {
  def hi(name: String): Unit
  def queue(name: String): Unit
  def accept(response: Boolean): Unit
}

class StartupStageImpl() extends StartupStage with StartupListener {
  private val mainScene = new StartupScene(this)
  val client: ActorSystem = ActorSystem("Client")
  var serverLobbyRef: Option[ActorRef] = None

  scene = mainScene
  onCloseRequest = _ => {
    withServerLobbyRef{ref => client.stop(ref)}
    client.terminate()
    println("Actor system terminated.")
    System.exit(0)
  }


  override def hi(name: String): Unit = {
    serverLobbyRef = Some(client.actorOf(MainViewActor.props(name)))
    withServerLobbyRef{ref =>
      ref ! Hi(name)
    }
  }

  override def queue(name: String): Unit = withServerLobbyRef{ref =>
    ref ! WannaQueue(name)
  }

  override def accept(response: Boolean): Unit = withServerLobbyRef{ref =>
    ref ! Ready("AAA")
  }

  private def withServerLobbyRef(f: ActorRef => Unit): Unit = {
    this.serverLobbyRef match {
      case Some(ref) => f(ref)
      case None => println(f"No server actor.")
    }
  }
}

object StartupStage {
  def apply(): StartupStage = new StartupStageImpl()
}
