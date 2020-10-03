package it.scalachikoro.client.views.stages.scenes.components

import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout.{BorderPane, VBox}

trait DicePanel extends BorderPane with ActionPanel {

  /**
   * Enable the second button.
   */
  def enable2dice()
}

trait DicePanelListener {
  def roll(n: Int)
}

object DicePanel {

  private[this] class DicePanelImpl(private[this] val listener: DicePanelListener) extends DicePanel {
    // From those btns users can roll for one or two dices.
    val diceLabel: Label = Label("Dice")
    val roll1Btn: Button = new Button("Roll one dice") {
      onAction = _ => listener roll 1 // TODO: Move this behaviour to another class.
    }
    val roll2Btn: Button = new Button("Roll two dices") {
      onAction = _ => listener roll 2
    }
    val diceContainer: VBox = new VBox() {
      spacing = defaultSpacing
    }
    diceContainer.children addAll(diceLabel, roll1Btn, roll2Btn)
    center = diceContainer

    /**
     * @inheritdoc
     */
    override def enable2dice(): Unit = roll2Btn setDisable false

    /**
     * @inheritdoc
     */
    override def enable(b: Boolean): Unit = {
      roll1Btn setDisable b
      roll2Btn setDisable b
    }
  }

  def apply(listener: DicePanelListener): DicePanel = new DicePanelImpl(listener)
}