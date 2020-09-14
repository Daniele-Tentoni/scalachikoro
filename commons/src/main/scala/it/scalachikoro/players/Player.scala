package it.scalachikoro.players

import it.scalachikoro.cards.Card

trait Player extends Identifiable {
  val name: String
}

case class PlayerKoro(override val name: String,override val id: String, cards: Seq[Card]) extends Player {
}