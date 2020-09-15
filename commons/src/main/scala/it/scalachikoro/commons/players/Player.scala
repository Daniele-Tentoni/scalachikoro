package it.scalachikoro.commons.players

import it.scalachikoro.commons.cards.BoardCard

trait Player extends Identifiable {
  val name: String
}

case class PlayerKoro(override val name: String, override val id: String, money: Int, cards: Seq[BoardCard]) extends Player {
  def receive(n: Int): PlayerKoro = copy(money = money + n)

  def give(n: Int): (PlayerKoro, Int) =
    if (n > money)
      (copy(money = 0), money)
    else
      (copy(money = money - n), n)
}