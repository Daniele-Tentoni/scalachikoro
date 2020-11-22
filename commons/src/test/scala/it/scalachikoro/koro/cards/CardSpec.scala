package it.scalachikoro.koro.cards

import org.scalatest.wordspec.AnyWordSpec

class CardSpec extends AnyWordSpec {
  "Icon conversion" when {
    "Submit a string" should {
      "produce RuntimeException if string is wrong" in {
        assertThrows[RuntimeException] {
          val i: Icon = "Other"
        }
      }

      /*object Others{
        case class Other() extends Icon
      }

      "produce RuntimeException if icon is wrong" in {
        assertThrows[RuntimeException] {
          val i: String = Others.Other()
        }
      }*/
    }
  }
}
