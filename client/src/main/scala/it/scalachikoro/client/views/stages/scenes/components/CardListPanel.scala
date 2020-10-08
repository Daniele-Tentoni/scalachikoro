package it.scalachikoro.client.views.stages.scenes.components

import it.scalachikoro.koro.cards.Card
import scalafx.geometry.Insets
import scalafx.scene.layout.{BorderPane, VBox}

trait CardListPanel extends BorderPane with ActionPanel {

}

trait CardListPanelListener extends ClickListener

object CardListPanel {

  private[this] class CardListPanelImpl(cards: Seq[Card], listener: CardListPanelListener) extends CardListPanel {
    padding = Insets(defaultSpacing)

    val cardsPanel: VBox = new VBox() {
      spacing = defaultSpacing
    }

    val gameCards: Seq[GameCard] = cards.map(c => GameCard(c, listener))
    gameCards.foreach(c => cardsPanel.children.add(c))

    center = cardsPanel

    /**
     * @inheritdoc
     */
    override def enable(b: Boolean): Unit = {
      gameCards.foreach(_.enable(b))
      cardsPanel.setDisable(!b)
    }
  }

  def apply(cards: Seq[Card], listener: CardListPanelListener): CardListPanel = new CardListPanelImpl(cards, listener)

}
