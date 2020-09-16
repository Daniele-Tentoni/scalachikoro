package it.scalachikoro.client.controllers

import akka.actor.ActorSystem
import scalafx.application.JFXApp

trait Controller {
  def start(app: JFXApp): Unit

  def stop(): Unit
}

class MainController() extends Controller {
  val client: ActorSystem = ActorSystem("Client")
  private val startup = new StartupController(client)

  override def start(app: JFXApp): Unit = {
    // val view = new View(app, startup)
    startup.start(app)
  }

  override def stop(): Unit = println("Controller ended.")
}
