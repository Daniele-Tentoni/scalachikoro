package it.scalachikoro.client.views.stages

import it.scalachikoro.client.controllers.GameEventListener
import it.scalachikoro.client.views.stages.scenes.GameScene

trait GameStage extends BaseStage

object GameStage {

  private class GameStageImpl(listener: GameEventListener) extends GameStage {
    private[this] val gameScene = GameScene(listener)

    scene = gameScene
    onCloseRequest = _ => System.exit(0)
  }

  def apply(listener: GameEventListener): GameStage = new GameStageImpl(listener)
}