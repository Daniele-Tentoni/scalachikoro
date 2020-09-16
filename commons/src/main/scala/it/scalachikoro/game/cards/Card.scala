package it.scalachikoro.game.cards

import it.scalachikoro.game.cards.CardType._
import it.scalachikoro.game.cards.InTurn.InTurnCode

sealed class InTurn(code: InTurnCode) {
  def check(turn: Boolean): Boolean =
    code == 2 ||
      turn && (code == 0) ||
      !turn && (code == 1)
}

object InTurn {
  type InTurnCode = Int

  case class PlayerTurn() extends InTurn(0)

  case class OtherPlayerTurn() extends InTurn(1)

  case class BothPlayerTurn() extends InTurn(2)

  case class NoPlayerTurn() extends InTurn(3)

  implicit def int2turn(i: Int): InTurn = i match {
    case 0 => PlayerTurn()
    case 1 => OtherPlayerTurn()
    case 2 => BothPlayerTurn()
    case _ => NoPlayerTurn()
  }
}

sealed class CardType(name: String, icon: String, activation: Seq[Int], inTurn: InTurn) {
  def trigger(n: Int, playersTurn: Boolean): Boolean = (activation contains n) && (inTurn check playersTurn)
}

object CardType {

  case class Landmark() extends CardType("Landmark", "L", Seq.empty, 0)

  case class PrimaryIndustry(activation: Seq[Int]) extends CardType("Primary Industry", "P", activation, 2)

  case class SecondaryIndustry(activation: Seq[Int]) extends CardType("Secondary Industry ", "S", activation, 0)

  case class Restaurants(activation: Seq[Int]) extends CardType("Restaurants", "R", activation, 1)

  case class Major(activation: Seq[Int]) extends CardType("Major Establishment", "M", activation, 0)

}

case class Card(name: String, cost: Int, cType: CardType, copies: Int) {
  def all: Seq[Card] = (0 until copies).map(_ => copy())

  // TODO: Implement trigger invocation.
  def trigger(n: Int, inTurn: Boolean): Int = if (cType.trigger(n, inTurn)) 1 else 0
}

object AimCard {
  def TrainStation: Card = Card("Train Station", 2, Landmark(), 1)

  def ShoppingHall: Card = Card("Shopping Hall", 2, Landmark(), 1)

  def AmusementPark: Card = Card("Amusement Park", 2, Landmark(), 1)

  def RadioTower: Card = Card("Radio Tower", 2, Landmark(), 1)

  def all: Seq[Card] = Seq(TrainStation, ShoppingHall, AmusementPark, RadioTower)
}

object Card {
  def WheatField: Card = Card("Wheat Field", 1, PrimaryIndustry(Seq(1)), 6)

  def Ranch: Card = Card("Ranch", 1, PrimaryIndustry(Seq(2)), 6)

  def Forest: Card = Card("Forest", 3, PrimaryIndustry(Seq(5)), 6)

  def Mine: Card = Card("Mine", 6, PrimaryIndustry(Seq(9)), 6)

  def AppleOrchard: Card = Card("Apple Orchard", 3, PrimaryIndustry(Seq(10)), 6)

  def Bakery: Card = Card("Bakery", 1, SecondaryIndustry(Seq(2, 3)), 6)

  def ConvStore: Card = Card("Convenience Store", 1, SecondaryIndustry(Seq(4)), 6)

  def CheeseFact: Card = Card("Cheese Factory", 1, SecondaryIndustry(Seq(7)), 6)

  def FurnitureFact: Card = Card("Furniture Factory", 1, SecondaryIndustry(Seq(8)), 6)

  def FruitMarket: Card = Card("Fruit and Vegetable Market", 1, SecondaryIndustry(Seq(11, 12)), 6)

  def Cafe: Card = Card("Café", 1, Restaurants(Seq(3)), 6)

  def FamilyRest: Card = Card("Family Restaurant", 1, Restaurants(Seq(9, 10)), 6)

  def Stadium: Card = Card("Stadium", 1, Major(Seq(6)), 4)

  def TVStation: Card = Card("TV Station", 1, Major(Seq(6)), 4)

  def BusinessCenter: Card = Card("Business Center", 1, Major(Seq(6)), 4)

  def starterCards = Seq(WheatField, Bakery)

  def all: Seq[Card] = WheatField.all ++ Ranch.all ++ Forest.all ++ Mine.all ++ AppleOrchard.all ++
    Bakery.all ++ ConvStore.all ++ CheeseFact.all ++ FurnitureFact.all ++ FruitMarket.all ++ Cafe.all ++
    FamilyRest.all ++ Stadium.all ++ TVStation.all ++ BusinessCenter.all
}