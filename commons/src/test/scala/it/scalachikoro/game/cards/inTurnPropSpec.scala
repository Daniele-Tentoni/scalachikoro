package it.scalachikoro.game.cards

import org.scalatest.matchers.should
import org.scalatest.prop.{TableDrivenPropertyChecks, TableFor3}
import org.scalatest.propspec.AnyPropSpec

class inTurnPropSpec
  extends AnyPropSpec
    with TableDrivenPropertyChecks
    with should.Matchers {
  val inTurns: TableFor3[Int, Boolean, Boolean] = Table(
    ("code", "turn", "res"),
    (0, true, true),
    (0, false, false),
    (1, true, false),
    (1, false, true),
    (2, true, true),
    (2, false, true),
    (3, true, false),
    (3, false, false))

  property("Check if player is in turn.") {
    forAll(inTurns) { (code, turn, result) =>
      val toCheck: InTurn = code
      toCheck check turn should be(result)
    }
  }
}
