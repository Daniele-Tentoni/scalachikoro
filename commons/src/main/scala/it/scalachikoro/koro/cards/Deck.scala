package it.scalachikoro.koro.cards

import scala.util.Random

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

  def shuffled: Deck = Deck(Random.shuffle(Card.all))

  def empty: Deck = Deck(Seq.empty)
}