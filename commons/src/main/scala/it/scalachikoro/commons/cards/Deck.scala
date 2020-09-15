package it.scalachikoro.commons.cards

import scala.util.Random

case class Deck(cards: Seq[BoardCard]) {
  def visibleCards: Seq[BoardCard] = cards take 15

  def pickCard(i: Int): (Deck, Option[BoardCard]) = {
    val cut = cards.drop(i)
    (copy(cards = cards.take(i) ++ cut.tail), cut.headOption)
  }

  def remaining: Int = cards.length

  def isEmpty: Boolean = cards.isEmpty
}

object Deck {
  def sorted: Deck = Deck(BoardCard.allBoardCards)

  def shuffled: Deck = Deck(Random.shuffle(BoardCard.allBoardCards))

  def empty: Deck = Deck(Seq.empty)
}