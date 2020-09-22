package it.scalachikoro.koro.cards

import it.scalachikoro.koro.cards.CardType._
import it.scalachikoro.koro.cards.Icon.{Bread, Cow, Cup, Factory, Fruit, Gear, Wheat}
import it.scalachikoro.koro.game.Operation
import it.scalachikoro.koro.players.PlayerKoro

sealed class CardType(name: String)

sealed class Icon(val icon: String)

object Icon {

  case class Wheat() extends Icon("Wheat")

  case class Cow() extends Icon("Cow")

  case class Gear() extends Icon("Gear")

  case class Bread() extends Icon("Bread")

  case class Factory() extends Icon("Factory")

  case class Fruit() extends Icon("Fruit")

  case class Cup() extends Icon("Cup")

  case class Major() extends Icon("Major")

}

object CardType {

  case class Landmark() extends CardType("Landmark")

  // TODO: This have fixed income.
  case class PrimaryIndustry(activation: Seq[Int]) extends CardType("Primary Industry") with MayTrigger

  // TODO: This produce for each instance of Primary
  case class SecondaryIndustry(activation: Seq[Int], subType: Icon) extends CardType("Secondary Industry ") with MayTrigger

  // TODO: This take income from other Players.
  case class Restaurants(activation: Seq[Int]) extends CardType("Restaurants") with MayTrigger

  case class Major(activation: Seq[Int]) extends CardType("Major Establishment") with MayTrigger

}

trait Acquirable {
  val cost: Int

  def canAcquire(money: Int): Boolean = money >= cost
}

trait MayTrigger {
  val activation: Seq[Int]

  def trigger(n: Int): Boolean = activation contains n
}

case class Card(name: String, cType: CardType, icon: Icon, cost: Int, income: Int, quantity: Int, img: String = "") extends Acquirable {
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
    case SecondaryIndustry(_, sub) => Operation.Receive(income * player.cards.count(_.icon.icon == sub.icon), player)
  }
}

object AimCard {

  def TrainStation: Card = Card("Train Station", Landmark(), Icon.Major(), 1, 1, 1)

  def ShoppingHall: Card = Card("Shopping Hall", Landmark(), Icon.Major(), 1, 1, 1)

  def AmusementPark: Card = Card("Amusement Park", Landmark(), Icon.Major(), 1, 1, 1)

  def RadioTower: Card = Card("Radio Tower", Landmark(), Icon.Major(), 1, 1, 1)

  def all: Seq[Card] = Seq(TrainStation, ShoppingHall, AmusementPark, RadioTower)
}

object Card {

  def WheatField: Card = Card("Wheat Field", PrimaryIndustry(Seq(1)), Wheat(), 1, 1, 6)

  def Ranch: Card = Card("Ranch", PrimaryIndustry(Seq(2)), Cow(), 1, 2, 6)

  def Forest: Card = Card("Forest", PrimaryIndustry(Seq(5)), Gear(), 3, 5, 6)

  def Mine: Card = Card("Mine", PrimaryIndustry(Seq(9)), Gear(), 6, 9, 6)

  def AppleOrchard: Card = Card("Apple Orchard", PrimaryIndustry(Seq(10)), Wheat(), 3, 10, 6)

  def Bakery: Card = Card("Bakery", SecondaryIndustry(Seq(2, 3), Wheat()), Bread(), 1, 1, 6)

  def ConvStore: Card = Card("Convenience Store", SecondaryIndustry(Seq(4), Wheat()), Bread(), 1, 1, 6)

  def CheeseFact: Card = Card("Cheese Factory", SecondaryIndustry(Seq(7), Wheat()), Factory(), 1, 1, 6)

  def FurnitureFact: Card = Card("Furniture Factory", SecondaryIndustry(Seq(8), Gear()), Factory(), 1, 1, 6)

  def FruitMarket: Card = Card("Fruit and Vegetable Market", SecondaryIndustry(Seq(11, 12), Wheat()), Fruit(), 1, 1, 6)

  def Cafe: Card = Card("Cafe'", Restaurants(Seq(3)), Cup(), 1, 1, 6)

  def FamilyRest: Card = Card("Family Restaurant", Restaurants(Seq(9, 10)), Cup(), 1, 1, 6)

  def Stadium: Card = Card("Stadium", Major(Seq(6)), Icon.Major(), 1, 1, 4)

  def TVStation: Card = Card("TV Station", Major(Seq(6)), Icon.Major(), 1, 1, 4)

  def BusinessCenter: Card = Card("Business Center", Major(Seq(6)), Icon.Major(), 1, 1, 4)

  def starterCards = Seq(WheatField, Bakery)

  def all: Seq[Card] = WheatField.all ++ Ranch.all ++ Forest.all ++ Mine.all ++ AppleOrchard.all ++
    Bakery.all ++ ConvStore.all ++ CheeseFact.all ++ FurnitureFact.all ++ FruitMarket.all ++ Cafe.all ++
    FamilyRest.all ++ Stadium.all ++ TVStation.all ++ BusinessCenter.all
}