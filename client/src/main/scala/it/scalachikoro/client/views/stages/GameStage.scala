package it.scalachikoro.client.views.stages

import it.scalachikoro.client.controllers.GamePanelListener
import it.scalachikoro.client.views.stages.scenes.GameScene
import it.scalachikoro.client.views.stages.scenes.components.{BoardEventListener, SideEventListener}
import it.scalachikoro.koro.cards.Card
import it.scalachikoro.koro.game.GameState
import it.scalachikoro.koro.players.{Player, PlayerKoro}
import scalafx.application.Platform

trait GameStage extends BaseStage with SideEventListener with BoardEventListener {
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

    /**
     * The player received moneys from server.
     *
     * @param n Money received.
     */
    override def receive(n: Int, from: String): Unit = gameScene receive(n, from)

    /**
     * The player must give moneys to another.
     *
     * @param n  Amount of moneys.
     * @param to Receiver.
     */
    override def give(n: Int, from: PlayerKoro, to: PlayerKoro): Unit = gameScene give (n, from, to)

    /**
     * A player have acquired a card.
     *
     * @param player Player that have done the operation.
     * @param card   Card acquired.
     */
    override def acquired(player: Player, card: Card): Unit = gameScene acquired(player, card)
  }

  def apply(startState: GameState, listener: GamePanelListener): GameStage = new GameStageImpl(startState, listener)
}