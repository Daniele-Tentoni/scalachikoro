package it.scalachikoro.client.views.stages.scenes

import it.scalachikoro.client.controllers.MainViewActorListener
import scalafx.geometry.Pos
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout.VBox

case class QueueScene(name: String, listener: MainViewActorListener) extends BaseScene {
  val usernameLabel: Label = Label(name)
  val btnQueue = new Button("Queue")
  btnQueue.onAction = _ => queue()

  val btnLeave = new Button("Leave queue")
  btnLeave.onAction = _ => enqueue()

  val center: VBox = new VBox()
  center.alignment = Pos.Center
  center.spacing = 10
  center.setMaxWidth(400)
  center.getChildren.addAll(usernameLabel, btnQueue, btnLeave)
  mainContent.center = center

  private def queue(): Unit = {
    bottomBar message "Entering queue"
    bottomBar loading true
    listener.queue(name)
  }

  def queued(n: Int): Unit = {
    bottomBar message f"Queued with other $n other players"
    bottomBar loading true
  }

  private def enqueue(): Unit = {
    bottomBar message "Leaving queue"
    bottomBar loading true
    listener.leaveQueue()
  }

  def enqueued(): Unit = {
    bottomBar message "Left queue"
    bottomBar loading false
  }
}