package it.scalachikoro.client.views.stages

import it.scalachikoro.client.controllers.MainViewActorListener
import it.scalachikoro.client.views.stages.scenes.StartupScene

trait StartupStage extends BaseStage

object StartupStage {

  // TODO: Document this.
  private class StartupStageImpl(listener: MainViewActorListener) extends StartupStage {
    private val mainScene = new StartupScene(listener)

    scene = mainScene
    onCloseRequest = _ => System.exit(0)
  }

  def apply(listener: MainViewActorListener): StartupStage = new StartupStageImpl(listener)
}