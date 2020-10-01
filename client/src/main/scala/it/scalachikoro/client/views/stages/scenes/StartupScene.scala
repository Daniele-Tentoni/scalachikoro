package it.scalachikoro.client.views.stages.scenes

import it.scalachikoro.client.controllers.listeners.MainViewActorListener
import it.scalachikoro.client.views.stages.scenes.components.IpTextFormatter
import scalafx.geometry.Pos
import scalafx.scene.control.{Button, Label, TextField}
import scalafx.scene.layout.VBox

// TODO: Create the companion object.
case class StartupScene(listener: MainViewActorListener) extends BaseScene {
  private[this] val maxLabelWidth = 300
  // TODO: Add a background.
  val serverLabel: Label = Label("Server")
  serverLabel.maxWidth = 300
  val serverField: TextField = new TextField {
    text = "0.0.0.0"
    textFormatter = IpTextFormatter()
    maxWidth = maxLabelWidth
  }

  val portLabel: Label = Label("Port")
  portLabel.maxWidth = maxLabelWidth
  val portField: TextField = new TextField {
    text = "47000"
    maxWidth = maxLabelWidth
  }

  val usernameLabel: Label = Label("Username")
  val usernameField: TextField = new TextField() {
    maxWidth = 500
  }
  val btnHi: Button = new Button("Hi") {
    onAction = _ => submit()
  }

  val center: VBox = new VBox() {
    alignment = Pos.Center
    spacing = 10d
  }
  center.getChildren.addAll(serverLabel, serverField, portLabel, portField, usernameLabel, usernameField, btnHi)

  // This take mainContent from the parent scene.
  mainContent.center = center

  private[this] def submit(): Unit = {
    val username: String = usernameField.getText
    val server: String = serverField.getText
    val port: String = portField.getText

    if (!username.isEmpty) {
      bottomBar message "Connecting to server"
      bottomBar loading true
      listener.connect(username, server, port)
    } else {
      bottomBar message "Username not present."
      bottomBar loading false
    }
  }
}
