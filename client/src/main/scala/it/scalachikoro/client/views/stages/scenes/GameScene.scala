package it.scalachikoro.client.views.stages.scenes

import it.scalachikoro.client.controllers.GameEventListener
import it.scalachikoro.client.views.stages.scenes.components.SidePanel
import it.scalachikoro.client.views.utils.KoroAlert
import it.scalachikoro.koro.game.GameState
import scalafx.geometry.Pos
import scalafx.scene.control.{Button, ButtonType, Label}
import scalafx.scene.layout.VBox

trait GameScene extends BaseScene {
  def updateGameState(state: GameState)
}

trait SideEventListener {
  def drop()
  def pass()
}

object GameScene {

  private class GameSceneImpl(listener: GameEventListener) extends GameScene with SideEventListener {
    // TODO: Add a background.

    val diceLabel: Label = Label("Dice")
    val roll1Btn: Button = new Button("Roll one dice") {
      onAction = _ => roll(1) // TODO: Move this behaviour to another class.
    }

    val roll2Btn: Button = new Button("Roll two dices") {
      onAction = _ => roll(2)
    }

    // TODO: Add a list of previous rolls.
    // TODO: Add the player card list.
    // TODO: Add the deck card list.

    val center: VBox = new VBox() {
      alignment = Pos.Center
      maxWidth = 400
      spacing = 10
    }
    center.getChildren.addAll(btnDrop, diceLabel, roll1Btn, roll2Btn)
    mainContent.center = center

    val rightBar: SidePanel = SidePanel(this)
    mainContent.right = rightBar

    private[this] def roll(n: Int): Unit = listener.roll(n)

    override def updateGameState(state: GameState): Unit = state match {
      case GameState.BrokenGameState(message) => KoroAlert.error("Error in GameState", message)
      case GameState.LocalGameState(player, others, cards) =>
        rightBar.username(player.name)
        rightBar.addHistory("Update game state")

      case _ =>
    }

    override def drop(): Unit = {
      val response = KoroAlert.confirmation("Drop game", "Are you sure to drop the game?") showAndWait()
      if (response.contains(ButtonType.OK)) {
        KoroAlert.info("Nooo", "Too bad. You can't drop.") showAndWait()
      } else {
        KoroAlert.info("Well", "Well... Very well!") showAndWait()
      }
    }

    override def pass(): Unit = {
      println("Pass")
    }
  }

  def apply(listener: GameEventListener): GameScene = new GameSceneImpl(listener)
}