package it.scalachikoro.game.matches

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class TurnSpec extends AnyWordSpec  with Matchers{
  "A Turn" when {
    val ints: Seq[Int] = Seq(1, 2, 3)
    var turn: Turn[Int] = Turn(ints)
    "created" should {
      "have items" in {
        assertResult(ints)(turn.all)
      }
      "start at first int" in {
        assertResult(ints.head)(turn.get)
      }
    }
    "already started" should {
      "go to next turn" in {
        assertResult(ints(1))(turn.next)
      }
      "another time" in {
        assertResult(ints(2))(turn.next)
      }
    }
    "at the end" should {
      "restart" in {
        assertResult(ints.head)(turn.next)
      }
    }
  }
}
