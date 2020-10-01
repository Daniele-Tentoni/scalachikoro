package it.scalachikoro.koro.players

import akka.actor.ActorRef
import it.scalachikoro.koro.cards.{AimCard, Card}
import it.scalachikoro.koro.game.Operation

trait Identifiable {
  def id: String
}

trait Referable {
  def actorRef: ActorRef
}

abstract sealed class Player() extends Identifiable {
  def name: String
}

case class PlayerRef(override val actorRef: ActorRef, override val id: String, override val name: String) extends Player() with Referable

case class PlayerKoro(override val id: String, name: String, money: Int, cards: Seq[Card]) extends Player() {
  def canAcquire(card: Card): Boolean =
    money >= card.cost && cards.count(c => c == card) < card.quantity

  def acquire(card: Card): PlayerKoro = if (canAcquire(card))
    copy(money = money - card.cost, cards = card +: cards)
  else
    this

  def calculateIncomes(n: Int): Seq[Operation] = cards.map(c =>
    if (c.trigger(n, turn = true)) c.income(this)
    else Operation.NoOperation())

  // TODO: Why I can't decrease here Player's moneys?
  def calculateTaxes(n: Int, other: PlayerKoro): Seq[Operation] = other.cards.foldLeft((Seq.empty[Operation], money)){
    (acc, card) => if(card.trigger(n, turn = false)) {
      val tax = if(acc._2 > card.income) card.income else acc._2
      (acc._1 :+ Operation.Give(tax, other), acc._2 - tax)
    } else {
      acc
    }
  }._1

  def receive(n: Int): PlayerKoro = copy(money = money + n)

  def give(n: Int): PlayerKoro = if(n < money) copy(money = money - n) else copy(money = 0)

  def hasWon: Boolean = cards.filter(AimCard.all contains _) == AimCard.all
}

object PlayerKoro {
  def apply(id: String, name: String, money: Int, cards: Seq[Card]): PlayerKoro = new PlayerKoro(id, name, money, cards)

  def init(id: String, name: String): PlayerKoro = new PlayerKoro(id, name, 0, Card.starterCards)

  def bank = new PlayerKoro("Bank", "Bank", 999, Seq.empty)
}