package it.scalachikoro.client.views.stages.scenes

import it.scalachikoro.client.controllers.MainViewActorListener
import it.scalachikoro.client.views.KoroAlert
import scalafx.beans.property.DoubleProperty
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.Scene
import scalafx.scene.control.{Button, Label, TextField}
import scalafx.scene.layout.{BorderPane, StackPane, VBox}

class StartupScene(listener: MainViewActorListener) extends Scene() {
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
  center.getChildren.addAll(usernameLabel, usernameField, btnHi, btnQueue)

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
}
