package it.scalachikoro.client

import it.scalachikoro.client.controllers.MainController
import scalafx.application.JFXApp

object Client extends JFXApp {
  val c = new MainController(this)
  c.start()
}
