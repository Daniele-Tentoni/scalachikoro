package it.scalachikoro.cards

import it.scalachikoro.cards.CardType.{Landmark, Major, PrimaryIndustry, Restaurants, SecondaryIndustry}

sealed class Trigger(color: String)

object Trigger {

  case class Blue() extends Trigger("Blue")

  case class Green() extends Trigger("Yellow")

  case class Red() extends Trigger("Red")

  case class Violet() extends Trigger("Violet")

  case class None() extends Trigger("Yellow")

}

sealed class CardType(name: String, icon: String)

object CardType {

  case class PrimaryIndustry(activation: Seq[Int]) extends CardType("Primary Industry", "P")

  case class SecondaryIndustry(activation: Seq[Int]) extends CardType("Secondary Industry ", "S")

  case class Restaurants(activation: Seq[Int]) extends CardType("Restaurants", "R")

  case class Major(activation: Seq[Int]) extends CardType("Major Establishment", "M")

  case class Landmark() extends CardType("Landmard", "L")

}

sealed class Card(name: String, cost: Int, cardType: CardType, copies: Int) {
  def all: Seq[Card] = (0 until copies).map(m => this.clone().asInstanceOf[Card])
}

object Card {

  case class TrainStation() extends Card("Train Station", 2, Landmark(), 1)

  case class ShoppingHall() extends Card("Shopping Hall", 2, Landmark(), 1)

  case class AmusementPark() extends Card("Amusement Park", 2, Landmark(), 1)

  case class RadioTower() extends Card("Radio Tower", 2, Landmark(), 1)

  def allAimCards: Seq[Card] = Seq(TrainStation(), ShoppingHall(), AmusementPark(), RadioTower())

  def starterCards = Seq(WheatField(), Bakery())

  def allBoardCards = WheatField().all ++ Ranch().all ++ Forest().all ++ Mine().all ++ AppleOrchard().all ++
    Bakery().all ++ ConvStore().all ++ CheeseFact().all ++ FurnitureFact().all ++ FruitMarket().all ++ Cafe().all ++
    FamilyRest().all ++ Stadium().all ++ TVStation().all ++ BusinessCenter().all

  case class WheatField() extends Card("Wheat Field", 1, PrimaryIndustry(Seq(1)), 6)

  case class Ranch() extends Card("Ranch", 1, PrimaryIndustry(Seq(2)), 6)

  case class Forest() extends Card("Forest", 3, PrimaryIndustry(Seq(5)), 6)

  case class Mine() extends Card("Mine", 6, PrimaryIndustry(Seq(9)), 6)

  case class AppleOrchard() extends Card("Apple Orchard", 3, PrimaryIndustry(Seq(10)), 6)

  case class Bakery() extends Card("Bakery", 1, SecondaryIndustry(Seq(2, 3)), 6)

  case class ConvStore() extends Card("Convenience Store", 1, SecondaryIndustry(Seq(4)), 6)

  case class CheeseFact() extends Card("Cheese Factory", 1, SecondaryIndustry(Seq(7)), 6)

  case class FurnitureFact() extends Card("Furniture Factory", 1, SecondaryIndustry(Seq(8)), 6)

  case class FruitMarket() extends Card("Fruit and Vegetable Market", 1, SecondaryIndustry(Seq(11, 12)), 6)

  case class Cafe() extends Card("Café", 1, Restaurants(Seq(3)), 6)

  case class FamilyRest() extends Card("Family Restaurant", 1, Restaurants(Seq(9, 10)), 6)

  case class Stadium() extends Card("Stadium", 1, Major(Seq(6)), 4)

  case class TVStation() extends Card("TV Station", 1, Major(Seq(6)), 4)

  case class BusinessCenter() extends Card("Business Center", 1, Major(Seq(6)), 4)

}