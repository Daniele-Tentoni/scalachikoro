package it.scalachikoro.koro.players

import it.scalachikoro.koro.cards.{AimCard, Card}
import it.scalachikoro.koro.game.Operation

case class PlayerKoro(override val id: String, name: String, money: Int, cards: Seq[Card]) extends Player() {
  def canAcquire(card: Card): Boolean =
    money >= card.cost && cards.count(c => c == card) < card.quantity

  def acquire(card: Card): PlayerKoro = if (canAcquire(card))
    copy(money = money - card.cost, cards = card +: cards)
  else
    this

  def calculateIncomes(n: Int): Seq[Operation] = cards.map(c =>
    if (c.trigger(n, turn = true)) c.income(this)
    else Operation.NoOperation)

  // TODO: Why I can't decrease here Player's moneys?
  def calculateTaxes(n: Int, other: PlayerKoro): Seq[Operation] = other.cards.foldLeft((Seq.empty[Operation], money)) {
    (acc, card) =>
      if (card.trigger(n, turn = false)) {
        val tax = if (acc._2 > card.income) card.income else acc._2
        val operations = acc._1 :+ Operation.Give(tax, other)
        val remaings = acc._2 - tax
        (operations, remaings)
      } else {
        acc
      }
  }._1

  def receive(n: Int): PlayerKoro = copy(money = money + n)

  def give(n: Int): PlayerKoro = if (n < money) copy(money = money - n) else copy(money = 0)

  def hasWon: Boolean = cards.filter(AimCard.all contains _) == AimCard.all
}

object PlayerKoro {
  def apply(id: String, name: String, money: Int, cards: Seq[Card]): PlayerKoro = new PlayerKoro(id, name, money, cards)

  def init(id: String, name: String): PlayerKoro = new PlayerKoro(id, name, 0, Card.starterCards)

  def bank = new PlayerKoro("Bank", "Bank", 999, Seq.empty)
}