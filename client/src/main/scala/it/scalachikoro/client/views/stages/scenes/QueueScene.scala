package it.scalachikoro.client.views.stages.scenes

import it.scalachikoro.client.controllers.MainViewActorListener
import scalafx.geometry.Pos
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout.VBox

case class QueueScene(name: String, listener: MainViewActorListener) extends BaseScene {
  val usernameLabel: Label = Label(name)
  val btnQueue: Button = new Button("Queue") {
    onAction = _ => queue()
  }

  val btnLeave: Button = new Button("Leave queue") {
    onAction = _ => enqueue()
  }

  val center: VBox = new VBox() {
    alignment = Pos.Center
    maxWidth = 400
    spacing = 10
  }

  center.getChildren.addAll(usernameLabel, btnQueue, btnLeave)
  mainContent.center = center

  private def queue(): Unit = {
    bottomBar message "Entering queue"
    bottomBar loading true
    listener queue name
  }

  def queued(n: Int): Unit = {
    bottomBar message f"Queued with other $n other players"
    bottomBar loading true
  }

  private def enqueue(): Unit = {
    bottomBar message "Leaving queue"
    bottomBar loading true
    listener leaveQueue()
  }

  def enqueued(): Unit = {
    bottomBar message "Left queue"
    bottomBar loading false
  }
}