package it.scalachikoro.client.controllers

import it.scalachikoro.client.views.View
import scalafx.application.JFXApp

trait Controller {
  def start(app: JFXApp): Unit

  def stop(): Unit
}

class MainController() extends Controller {
  private val startup = new StartupController

  override def start(app: JFXApp): Unit = {
    val view = new View(app)
    startup.start(app)
  }

  override def stop(): Unit = println("Controller ended.")
}
