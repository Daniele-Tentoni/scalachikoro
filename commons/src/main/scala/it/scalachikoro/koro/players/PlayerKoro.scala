package it.scalachikoro.koro.players

import it.scalachikoro.koro.cards.{AimCard, Card, CardType}

case class PlayerKoro(override val id: String, name: String, money: Int, boardCards: Seq[Card]) extends Player {
  def canAcquire(card: Card): Boolean =
    money >= card.cost && boardCards.count(c => c == card) < card.copies

  def acquire(card: Card): PlayerKoro = if (canAcquire(card))
    copy(money = money - card.cost, boardCards = card +: boardCards)
  else
    this

  def receive(n: Int): PlayerKoro = copy(money = money + n)

  def give(n: Int): (PlayerKoro, Int) =
    if (n > money)
      (copy(money = 0), money)
    else
      (copy(money = money - n), n)

  def hasWon: Boolean = boardCards.filter(_.cType match {
    case CardType.Landmark() => true
    case _ => false
  }) == AimCard.all
}

object PlayerKoro {
  def init(id: String, name: String): PlayerKoro = new PlayerKoro(id, name, 0, Card.starterCards)
}