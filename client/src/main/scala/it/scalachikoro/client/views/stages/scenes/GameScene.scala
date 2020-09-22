package it.scalachikoro.client.views.stages.scenes

import it.scalachikoro.client.controllers.GameEventListener
import it.scalachikoro.client.views.utils.KoroAlert
import scalafx.geometry.Pos
import scalafx.scene.control.{Button, ButtonType, Label}
import scalafx.scene.layout.VBox

trait GameScene extends BaseScene

object GameScene {

  private class GameSceneImpl(listener: GameEventListener) extends GameScene {
    // TODO: Add a background.
    val usernameLabel: Label = Label("Username")
    val btnDrop: Button = new Button("Drop game")
    btnDrop.onAction = _ => drop()

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

    val center: VBox = new VBox()
    center.alignment = Pos.Center
    center.spacing = 10
    center.setMaxWidth(400)
    center.getChildren.addAll(usernameLabel, btnDrop, diceLabel, roll1Btn, roll2Btn)
    mainContent.center = center

    private[this] def roll(n: Int): Unit = listener.roll(n)
  }

  private[this] def drop(): Unit = {
    val response = KoroAlert.confirmation("Drop game", "Are you sure to drop the game?") showAndWait()
    if (response.contains(ButtonType.OK)) {
      KoroAlert.info("Nooo", "Too bad. You can't drop.") showAndWait()
    } else {
      KoroAlert.info("Well", "Well... Very well!") showAndWait()
    }
  }

  def apply(listener: GameEventListener): GameScene = new GameSceneImpl(listener)
}