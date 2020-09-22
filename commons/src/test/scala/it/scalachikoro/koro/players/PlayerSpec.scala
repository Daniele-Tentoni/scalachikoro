package it.scalachikoro.koro.players

import it.scalachikoro.koro.cards.Card
import org.scalatest.funspec.AnyFunSpec

class PlayerSpec extends AnyFunSpec {
  describe("A Player") {
    val player = PlayerKoro("Test", "1", 0, Seq.empty[Card])
    it("Doesn't have money") {
      assertResult(0)(player.money)
    }
    val more = player.receive(4)
    it("Have money") {
      assertResult(4)(more.money)
    }
    val less = more.give(2)
    it("Give some money") {
      assertResult(2)(less.money)
    }
    val nothing = less.give(3)
    it("Give all money") {
      assertResult(0)(nothing.money)
    }
  }
}
