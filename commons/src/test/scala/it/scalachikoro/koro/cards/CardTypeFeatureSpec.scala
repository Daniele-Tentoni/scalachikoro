package it.scalachikoro.koro.cards

import org.scalatest.GivenWhenThen
import org.scalatest.featurespec.AnyFeatureSpec

class CardTypeFeatureSpec extends AnyFeatureSpec with GivenWhenThen {
  Feature("CardType") {
    Scenario("Must trigger") {
      Given("""Il trigger dell'effetto {int}""")
      val inTurn = 0
      When("""al lancio del dado {int} o {int}""")
      val activations = Seq(1, 2)
      val trigger = new CardType()
      Then("""lanci il dado per {int} in turno {boolean}""")
      val roll = 1
      val playerTurn = true
      And("""l'effetto deve {boolean}""")
      pending
    }
  }
}
