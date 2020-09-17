package it.scalachikoro.client.controllers

import akka.actor.ActorSystem
import scalafx.application.JFXApp

class MainController(app: JFXApp) extends Controller {
  val client: ActorSystem = ActorSystem("Client")
  val startup = new StartupController(client, app)

  /**
   * @inheritdoc
   */
  override def start(): Unit = {
    startup.start()
    println("MainController started.")
  }

  /**
   * @inheritdoc
   */
  override def stop(): Unit = {
    startup.stop()
    println("MainController ended.")
  }
}
