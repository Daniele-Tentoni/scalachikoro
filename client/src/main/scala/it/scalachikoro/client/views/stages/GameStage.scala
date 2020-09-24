package it.scalachikoro.client.views.stages

import it.scalachikoro.client.controllers.GameEventListener
import it.scalachikoro.client.views.stages.scenes.GameScene
import it.scalachikoro.koro.game.GameState

trait GameStage extends BaseStage {
  def updateGameState(state: GameState)
}

object GameStage {

  private[this] class GameStageImpl(listener: GameEventListener) extends GameStage {
    private[this] val gameScene = GameScene(listener)

    scene = gameScene
    onCloseRequest = _ => System.exit(0)

    override def updateGameState(state: GameState): Unit = gameScene.updateGameState(state)
  }

  def apply(listener: GameEventListener): GameStage = new GameStageImpl(listener)
}