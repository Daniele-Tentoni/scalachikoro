package it.scalachikoro.client.views.stages.scenes

import it.scalachikoro.client.controllers.MainViewActorListener
import scalafx.beans.property.DoubleProperty
import scalafx.geometry.{Insets, Pos}
import scalafx.scene.Scene
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout.{BorderPane, StackPane, VBox}

trait QueueScene extends Scene

object QueueScene {

  private class QueueSceneImpl(name: String, listener: MainViewActorListener) extends QueueScene {
    val usernameLabel: Label = Label(name)
    val btnQueue = new Button("Queue")
    btnQueue.onAction = _ => enqueue()

    val btnLeave = new Button("Leave queue")
    btnLeave.onAction = _ => leave()


    val center: VBox = new VBox()
    center.alignment = Pos.Center
    center.spacing = 10
    center.setMaxWidth(400)
    center.getChildren.addAll(usernameLabel, btnQueue, btnLeave)

    val mainContent: BorderPane = new BorderPane()
    mainContent.prefWidth <== DoubleProperty(800)
    mainContent.maxHeight <== DoubleProperty(640)
    mainContent.setPadding(Insets(5))
    mainContent.center = center

    val rootContent = new StackPane()
    rootContent.getChildren.addAll(mainContent)
    root = rootContent
    content = Seq(mainContent)

    private def enqueue(): Unit = listener.queue(name)

    private def leave(): Unit = listener.leaveQueue()
  }

  def apply(name: String, listener: MainViewActorListener): QueueScene = new QueueSceneImpl(name, listener)
}