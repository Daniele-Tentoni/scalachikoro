package it.scalachikoro.client.views.stages.scenes.components

import it.scalachikoro.koro.cards.Card
import scalafx.scene.layout.StackPane

trait GameCard {

}

object GameCard{
  private[this] class GameCardImpl(card: Card) extends StackPane with ActionPanel{
    /**
     * Enable or not all controls inside the panel.
     *
     * @param b Mode.
     */
    override def enable(b: Boolean): Unit = ???
  }
}
