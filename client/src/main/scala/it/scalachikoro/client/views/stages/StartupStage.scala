package it.scalachikoro.client.views.stages

import it.scalachikoro.client.controllers.MainViewActorListener
import it.scalachikoro.client.views.stages.scenes.StartupScene

trait StartupStage extends BaseStage

class StartupStageImpl(listener: MainViewActorListener) extends StartupStage {
  private val mainScene = new StartupScene(listener)

  scene = mainScene
  onCloseRequest = _ => { System.exit(0) }
}

object StartupStage {
  def apply(listener: MainViewActorListener): StartupStage = new StartupStageImpl(listener)
}