package it.scalachikoro.commons.cards

import it.scalachikoro.commons.cards.CardType._
import it.scalachikoro.commons.cards.InTurn.InTurnCode

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

case class BoardCard(name: String, cost: Int, cType: CardType, copies: Int) {
  def all: Seq[BoardCard] = (0 until copies).map(_ => copy())

  // TODO: Implement trigger invocation.
  def trigger(n: Int, inTurn: Boolean): Int = if (cType.trigger(n, inTurn)) 1 else 0
}

object AimCard {
  def TrainStation: BoardCard = BoardCard("Train Station", 2, Landmark(), 1)

  def ShoppingHall: BoardCard = BoardCard("Shopping Hall", 2, Landmark(), 1)

  def AmusementPark: BoardCard = BoardCard("Amusement Park", 2, Landmark(), 1)

  def RadioTower: BoardCard = BoardCard("Radio Tower", 2, Landmark(), 1)

  def allAimCards: Seq[BoardCard] = Seq(TrainStation, ShoppingHall, AmusementPark, RadioTower)
}

object BoardCard {
  def WheatField: BoardCard = BoardCard("Wheat Field", 1, PrimaryIndustry(Seq(1)), 6)

  def Ranch: BoardCard = BoardCard("Ranch", 1, PrimaryIndustry(Seq(2)), 6)

  def Forest: BoardCard = BoardCard("Forest", 3, PrimaryIndustry(Seq(5)), 6)

  def Mine: BoardCard = BoardCard("Mine", 6, PrimaryIndustry(Seq(9)), 6)

  def AppleOrchard: BoardCard = BoardCard("Apple Orchard", 3, PrimaryIndustry(Seq(10)), 6)

  def Bakery: BoardCard = BoardCard("Bakery", 1, SecondaryIndustry(Seq(2, 3)), 6)

  def ConvStore: BoardCard = BoardCard("Convenience Store", 1, SecondaryIndustry(Seq(4)), 6)

  def CheeseFact: BoardCard = BoardCard("Cheese Factory", 1, SecondaryIndustry(Seq(7)), 6)

  def FurnitureFact: BoardCard = BoardCard("Furniture Factory", 1, SecondaryIndustry(Seq(8)), 6)

  def FruitMarket: BoardCard = BoardCard("Fruit and Vegetable Market", 1, SecondaryIndustry(Seq(11, 12)), 6)

  def Cafe: BoardCard = BoardCard("Café", 1, Restaurants(Seq(3)), 6)

  def FamilyRest: BoardCard = BoardCard("Family Restaurant", 1, Restaurants(Seq(9, 10)), 6)

  def Stadium: BoardCard = BoardCard("Stadium", 1, Major(Seq(6)), 4)

  def TVStation: BoardCard = BoardCard("TV Station", 1, Major(Seq(6)), 4)

  def BusinessCenter: BoardCard = BoardCard("Business Center", 1, Major(Seq(6)), 4)

  def starterCards = Seq(WheatField, Bakery)

  def allBoardCards: Seq[BoardCard] = WheatField.all ++ Ranch.all ++ Forest.all ++ Mine.all ++ AppleOrchard.all ++
    Bakery.all ++ ConvStore.all ++ CheeseFact.all ++ FurnitureFact.all ++ FruitMarket.all ++ Cafe.all ++
    FamilyRest.all ++ Stadium.all ++ TVStation.all ++ BusinessCenter.all
}