package it.scalachikoro.client.views.stages

import it.scalachikoro.client.controllers.MainViewActorListener
import it.scalachikoro.client.views.stages.scenes.{QueueScene, StartupScene}
import scalafx.scene.Scene

trait StartupStage extends BaseStage {
  def goToQueueScene(name: String)

  def goToMainScene()
}

object StartupStage {

  // TODO: Document this.
  private class StartupStageImpl(listener: MainViewActorListener) extends StartupStage {
    private val mainScene: Scene = new StartupScene(listener)
    private var currentScene: Scene = mainScene // TODO: Change to a Scene Stack?

    scene = mainScene
    onCloseRequest = _ => System.exit(0)

    override def goToQueueScene(name: String): Unit = {
      val queueScene = QueueScene(name, listener)
      scene = queueScene
      currentScene = queueScene
    }

    override def goToMainScene(): Unit = {
      scene = mainScene
      currentScene = mainScene
    }
  }



  def apply(listener: MainViewActorListener): StartupStage = new StartupStageImpl(listener)
}