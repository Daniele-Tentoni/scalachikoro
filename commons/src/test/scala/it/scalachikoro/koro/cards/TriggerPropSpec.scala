package it.scalachikoro.koro.cards

import it.scalachikoro.koro.cards.Icon.icon2string
import org.scalatest.matchers.should
import org.scalatest.prop.{TableDrivenPropertyChecks, TableFor1}
import org.scalatest.propspec.AnyPropSpec

class TriggerPropSpec
  extends AnyPropSpec
    with TableDrivenPropertyChecks
    with should.Matchers {
  val inTurns: TableFor1[String] = Table(
    "icon",
    "Wheat",
    "Cow",
    "Gear",
    "Bread",
    "Factory",
    "Fruit",
    "Cup",
    "Major")

  property("Check if all icon are traslable.") {
    forAll(inTurns) { code =>
      val icon: Icon = code
      val str = icon2string(icon)
      str should be(code)
    }
  }
}
