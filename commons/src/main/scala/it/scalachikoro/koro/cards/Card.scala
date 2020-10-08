package it.scalachikoro.koro.cards

import it.scalachikoro.koro.cards.CardType._
import it.scalachikoro.koro.cards.Icon._
import it.scalachikoro.koro.game.Operation
import it.scalachikoro.koro.players.PlayerKoro

trait Icon

object Icon {
  val wheat = "Wheat"
  val cow = "Cow"
  val gear = "Gear"
  val bread = "Bread"
  val factory = "Factory"
  val fruit = "Fruit"
  val cup = "Cup"
  val major = "Major"

  case class Wheat() extends Icon

  case class Cow() extends Icon

  case class Gear() extends Icon

  case class Bread() extends Icon

  case class Factory() extends Icon

  case class Fruit() extends Icon

  case class Cup() extends Icon

  case class Major() extends Icon

  implicit def string2icon(s: String): Icon = s match {
    case Icon.wheat => Wheat()
    case Icon.cow => Cow()
    case Icon.gear => Gear()
    case Icon.bread => Bread()
    case Icon.factory => Factory()
    case Icon.fruit => Fruit()
    case Icon.cup => Cup()
    case Icon.major => Major()
    case _ => throw new RuntimeException()
  }

  implicit def icon2string(s: Icon): String = s match {
    case Wheat() => Icon.wheat
    case Cow() => Icon.cow
    case Gear() => Icon.gear
    case Bread() => Icon.bread
    case Factory() => Icon.factory
    case Fruit() => Icon.fruit
    case Cup() => Icon.cup
    case Major() => Icon.major
    case _ => throw new RuntimeException()
  }

}

sealed class CardType()

object CardType {

  /**
   * A Landmark Card Type.
   * His color is yellow.
   */
  case class Landmark() extends CardType()

  /**
   * A Primary Industry Card Type.
   * His color is blue.
   *
   * @param activation Sequence of numbers that trigger the card.
   */
  case class PrimaryIndustry(activation: Seq[Int]) extends CardType() with MayTrigger

  /**
   * A Secondary Industry Card Type.
   * His color is green.
   *
   * @param activation Sequence of numbers that trigger the card.
   * @param subType    Icon that stack with this.
   */
  case class SecondaryIndustry(activation: Seq[Int], subType: Icon) extends CardType() with MayTrigger

  /**
   * A Restaurant Card Type.
   * His color is red.
   *
   * @param activation Sequence of numbers that trigger the card.
   */
  case class Restaurants(activation: Seq[Int]) extends CardType() with MayTrigger

  /**
   * A Major Card Type.
   * His color is violet.
   *
   * @param activation Sequence of numbers that trigger the card.
   */
  case class Major(activation: Seq[Int]) extends CardType() with MayTrigger

}

trait Acquirable {
  val cost: Int

  def canAcquire(money: Int): Boolean = money >= cost
}

trait MayTrigger {
  val activation: Seq[Int]

  def trigger(n: Int): Boolean = activation contains n
}

case class Card(name: String, cType: CardType, icon: Icon, cost: Int, income: Int, quantity: Int) extends Acquirable {
  def all: Seq[Card] = (0 until quantity).map(_ => copy())

  def trigger(n: Int, turn: Boolean): Boolean = cType match {
    case PrimaryIndustry(activation) => activation contains n
    case Restaurants(activation) => (activation contains n) && !turn
    case SecondaryIndustry(activation, _) => (activation contains n) && turn
    case _ => false
  }

  def income(player: PlayerKoro): Operation = cType match {
    case Restaurants(_) => Operation.Give(income, player)
    case PrimaryIndustry(_) => Operation.Receive(income, player)
    case SecondaryIndustry(_, sub) => Operation.Receive(income * player.cards.count(_.icon == sub), player)
    case _ => Operation.NoOperation
  }
}

object AimCard {

  def trainStation: Card = Card("Train Station", Landmark(), Icon.Major(), 1, 1, 1)

  def shoppingHall: Card = Card("Shopping Hall", Landmark(), Icon.Major(), 1, 1, 1)

  def amusementPark: Card = Card("Amusement Park", Landmark(), Icon.Major(), 1, 1, 1)

  def radioTower: Card = Card("Radio Tower", Landmark(), Icon.Major(), 1, 1, 1)

  def all: Seq[Card] = Seq(trainStation, shoppingHall, amusementPark, radioTower)
}

object Card {

  def wheatField: Card = Card("Wheat Field", PrimaryIndustry(Seq(1)), Wheat(), 1, 1, 6)

  def ranch: Card = Card("Ranch", PrimaryIndustry(Seq(2)), Cow(), 1, 2, 6)

  def forest: Card = Card("Forest", PrimaryIndustry(Seq(5)), Gear(), 3, 5, 6)

  def mine: Card = Card("Mine", PrimaryIndustry(Seq(9)), Gear(), 6, 9, 6)

  def appleOrchard: Card = Card("Apple Orchard", PrimaryIndustry(Seq(10)), Wheat(), 3, 10, 6)

  def bakery: Card = Card("Bakery", SecondaryIndustry(Seq(2, 3), Wheat()), Bread(), 1, 1, 6)

  def convStore: Card = Card("Convenience Store", SecondaryIndustry(Seq(4), Wheat()), Bread(), 1, 1, 6)

  def cheeseFact: Card = Card("Cheese Factory", SecondaryIndustry(Seq(7), Wheat()), Factory(), 1, 1, 6)

  def furnitureFact: Card = Card("Furniture Factory", SecondaryIndustry(Seq(8), Gear()), Factory(), 1, 1, 6)

  def fruitMarket: Card = Card("Fruit and Vegetable Market", SecondaryIndustry(Seq(11, 12), Wheat()), Fruit(), 1, 1, 6)

  def cafe: Card = Card("Cafe'", Restaurants(Seq(3)), Cup(), 1, 1, 6)

  def familyRest: Card = Card("Family Restaurant", Restaurants(Seq(9, 10)), Cup(), 1, 1, 6)

  def stadium: Card = Card("Stadium", CardType.Major(Seq(6)), Icon.Major(), 1, 1, 4)

  def tVStation: Card = Card("TV Station", CardType.Major(Seq(6)), Icon.Major(), 1, 1, 4)

  def businessCenter: Card = Card("Business Center", CardType.Major(Seq(6)), Icon.Major(), 1, 1, 4)

  def starterCards = Seq(wheatField, bakery)

  def all: Seq[Card] = wheatField.all ++ ranch.all ++ forest.all ++ mine.all ++ appleOrchard.all ++
    bakery.all ++ convStore.all ++ cheeseFact.all ++ furnitureFact.all ++ fruitMarket.all ++ cafe.all ++
    familyRest.all ++ stadium.all ++ tVStation.all ++ businessCenter.all
}