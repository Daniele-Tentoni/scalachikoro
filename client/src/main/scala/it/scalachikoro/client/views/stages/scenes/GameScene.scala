package it.scalachikoro.client.views.stages.scenes

import it.scalachikoro.client.controllers.GamePanelListener
import it.scalachikoro.client.views.stages.scenes.components.{BoardEventListener, BoardPanel, DicePanelListener, SideEventListener, SidePanel}
import it.scalachikoro.client.views.utils.KoroAlert
import it.scalachikoro.koro.cards.Card
import it.scalachikoro.koro.game.GameState
import it.scalachikoro.koro.game.GameState.{BrokenGameState, LocalGameState}
import it.scalachikoro.koro.players.{Player, PlayerKoro}

trait GameScene extends BaseScene with GameEventListener {
  def updateGameState(state: GameState)
}

trait SidePanelListener extends DicePanelListener{
  def drop()

  def pass()
}

trait GameEventListener extends BoardEventListener with SideEventListener

object GameScene {

  private[this] class GameSceneImpl(startState: GameState, listener: GamePanelListener) extends GameScene {
    // TODO: Add a background.
    // TODO: Add a list of previous rolls.
    // TODO: Add the player card list.
    // TODO: Add the deck card list.

    val board: BoardPanel = startState match {
      case b: BrokenGameState => BoardPanel(b)
      case a: LocalGameState => BoardPanel(a, listener)
    }
    mainContent.center = board

    val side: SidePanel = SidePanel(listener)
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

    /**
     * @inheritdoc
     */
    override def diceRolled(n: Int): Unit = side diceRolled n

    /**
     * @inheritdoc
     */
    override def receive(n: Int, from: String): Unit = board receive (n, from)

    /**
     * @inheritdoc
     */
    override def give(n: Int, from: PlayerKoro, to: PlayerKoro): Unit = board give (n, from, to)

    /**
     * @inheritdoc
     */
    override def acquired(player: Player, card: Card): Unit = board acquired(player, card)
  }

  def apply(startState: GameState, listener: GamePanelListener): GameScene = new GameSceneImpl(startState: GameState, listener)
}