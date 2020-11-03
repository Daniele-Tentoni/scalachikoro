package it.scalachikoro.koro.cards

import it.scalachikoro.Utils

case class Deck(cards: Seq[Card]) {
  def visibleCards: Seq[Card] = cards take 15

  def pickCard(i: Int): (Deck, Option[Card]) = cards match {
    case Nil => (this, None)
    case _ =>
      val cut = cards drop i
      (copy(cards = (cards take i) ++ cut.tail), cut.headOption)
  }

  def remaining: Int = cards.length

  def isEmpty: Boolean = cards.isEmpty
}

object Deck {
  def sorted: Deck = Deck(Card.all)

  def shuffled: Deck = Deck(Utils.secureShuffle(Card.all))

  def empty: Deck = Deck(Seq.empty)
}