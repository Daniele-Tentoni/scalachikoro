package it.scalachikoro.client.views.stages.scenes

import it.scalachikoro.client.controllers.MainViewActorListener
import it.scalachikoro.client.views.stages.scenes.components.IpTextFormatter
import it.scalachikoro.client.views.utils.KoroAlert
import scalafx.geometry.Pos
import scalafx.scene.control.{Button, Label, TextField}
import scalafx.scene.layout.VBox

// TODO: Create the companion object.
class StartupScene(listener: MainViewActorListener) extends BaseScene() {
  // TODO: Add a background.
  val serverLabel: Label = Label("Server")
  val serverField: TextField = new TextField {
    text = "0.0.0.0"
    textFormatter = IpTextFormatter()
  }

  val portLabel: Label = Label("Port")
  val portField: TextField = new TextField {
    text = "47000"
  }

  val usernameLabel: Label = Label("Username")
  val usernameField: TextField = new TextField()
  val btnHi: Button = new Button("Hi")
  btnHi.onAction = _ => submit()

  val center: VBox = new VBox()
  center.alignment = Pos.Center
  center.spacing = 10
  // center.setMaxWidth(400)
  center.getChildren.addAll(serverLabel, serverField, portLabel, portField, usernameLabel, usernameField, btnHi)

  // This take mainContent from the parent scene.
  mainContent.center = center

  private def submit(): Unit = {
    val username: String = usernameField.getText
    val server: String = serverField.getText
    val port: String = portField.getText

    if (!username.isEmpty) {
      bottomBar message "Connecting to server"
      listener.connect(username, server, port)
    } else {
      KoroAlert info("Input error", "Some input error") showAndWait()
    }
  }
}
