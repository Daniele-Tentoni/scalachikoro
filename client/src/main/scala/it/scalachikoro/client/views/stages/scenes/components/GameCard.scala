package it.scalachikoro.client.views.stages.scenes.components

import it.scalachikoro.koro.cards.Card
import scalafx.geometry.Pos
import scalafx.scene.image.{Image, ImageView}
import scalafx.scene.layout.StackPane

trait GameCard extends StackPane with ActionPanel

trait ClickListener {
  def onClick(card: Card)
}

object GameCard {

  implicit class RichCard(c: Card) {
    def imgPath() = f"/img/cards/${c.name.replace(" ", "_").toLowerCase}_card.png"
  }

  private[this] class GameCardImpl(val card: Card, listener: ClickListener) extends GameCard {
    val imgPath: String = card.imgPath()
    println(imgPath)
    val cardImage: ImageView = new ImageView(new Image(imgPath)) {
      fitHeight = 100
      preserveRatio = true
    }

    this.setAlignment(Pos.TopRight)
    this.getStyleClass.add("baseCard")
    this.getChildren.add(cardImage)
    this.setOnMouseClicked(_ => listener.onClick(card))

    /**
     * @inheritdoc
     */
    override def enable(b: Boolean): Unit = if (b) {
      this.setOnMouseClicked(e => e.consume())
    } else {
      this.setOnMouseClicked(_ => listener.onClick(card))
    }
  }

  def apply(card: Card, listener: ClickListener): GameCard = new GameCardImpl(card, listener)
}
