package it.scalachikoro.client.views.stages.scenes

import it.scalachikoro.client.controllers.GamePanelListener
import it.scalachikoro.client.views.stages.scenes.components.{BoardPanel, DicePanelListener, SideEventListener, SidePanel}
import it.scalachikoro.client.views.utils.KoroAlert
import it.scalachikoro.koro.game.GameState
import scalafx.scene.control.ButtonType

trait GameScene extends BaseScene with SidePanelListener with SideEventListener {
  def updateGameState(state: GameState)
}

trait SidePanelListener extends DicePanelListener {
  def drop()

  def pass()
}

object GameScene {

  private class GameSceneImpl(startState: GameState, listener: GamePanelListener) extends GameScene {
    // TODO: Add a background.
    // TODO: Add a list of previous rolls.
    // TODO: Add the player card list.
    // TODO: Add the deck card list.

    val board: BoardPanel = BoardPanel()
    mainContent.center = board

    val side: SidePanel = SidePanel(this)
    mainContent.right = side
    updateGameState(startState)

    override def updateGameState(state: GameState): Unit = state match {
      case GameState.BrokenGameState(message) => KoroAlert.error("Error in GameState", message)
      case GameState.LocalGameState(player, others, cards) =>
        side.username(player.name)
        // TODO: Update cards in the middle of the board.
        side.addHistory("Update game state")
      case _ =>
    }

    override def drop(): Unit = {
      val response = KoroAlert.confirmation("Drop game", "Are you sure to drop the game?") showAndWait()
      if (response.contains(ButtonType.OK)) {
        KoroAlert.info("No!!!", "Too bad. You can't drop.") showAndWait()
      } else {
        KoroAlert.info("Well", "Well... Very well!") showAndWait()
      }
    }

    override def pass(): Unit = {
      println("Pass")
    }

    override def roll(n: Int): Unit = listener roll n

    /**
     * Notify the view of a dice roll.
     *
     * @param n Dice result.
     */
    override def diceRolled(n: Int): Unit = side diceRolled n
  }

  def apply(startState: GameState, listener: GamePanelListener): GameScene = new GameSceneImpl(startState: GameState, listener)
}