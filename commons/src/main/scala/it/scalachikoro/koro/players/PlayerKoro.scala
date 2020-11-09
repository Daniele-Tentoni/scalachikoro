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
        val moneys = acc._2
        val tax = if (moneys > card.income) card.income else acc._2
        (acc._1 :+ Operation.Give(tax, other), moneys - tax)
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

  val bankName = "Bank"

  def bank: PlayerKoro = new PlayerKoro(bankName, bankName, 999, Seq.empty)
}