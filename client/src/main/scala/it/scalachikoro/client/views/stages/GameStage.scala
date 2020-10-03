package it.scalachikoro.client.views.stages

import it.scalachikoro.client.controllers.GamePanelListener
import it.scalachikoro.client.views.stages.scenes.GameScene
import it.scalachikoro.client.views.stages.scenes.components.DiceEventListener
import it.scalachikoro.koro.game.GameState
import scalafx.application.Platform

trait GameStage extends BaseStage with DiceEventListener {
  def updateGameState(state: GameState)
}

object GameStage {

  private[this] class GameStageImpl(startState: GameState, listener: GamePanelListener) extends GameStage {
    private[this] val gameScene = GameScene(startState, listener)

    Platform.runLater {scene = gameScene}

    onCloseRequest = _ => System.exit(0)

    override def updateGameState(state: GameState): Unit = gameScene.updateGameState(state)

    /**
     * Notify the view of a dice roll.
     *
     * @param n Dice result.
     */
    override def diceRolled(n: Int): Unit = gameScene diceRolled n
  }

  def apply(startState: GameState, listener: GamePanelListener): GameStage = new GameStageImpl(startState, listener)
}