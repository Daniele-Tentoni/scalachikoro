package it.scalachikoro.client.views.stages.scenes.components

import scalafx.geometry.Insets
import scalafx.scene.control.Label
import scalafx.scene.layout.{BorderPane, HBox}

trait BoardPanel extends BorderPane with ActionPanel {

}

object BoardPanel {
  private[this] class BoardPanelImpl() extends BoardPanel {
    padding = Insets(defaultSpacing * 2)
    val playerCards: Label = Label("cards")
    val otherPlayersCards: Label = Label("others")
    val labelContainer: HBox = new HBox() {
      spacing = defaultSpacing
    }
    labelContainer.children addAll(playerCards, otherPlayersCards)

    center = labelContainer

    /**
     * Enable or not all controls inside the panel.
     *
     * @param b Mode.
     */
    override def enable(b: Boolean): Unit = ???
  }

  def apply(): BoardPanel = new BoardPanelImpl()
}