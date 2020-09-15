package it.scalachikoro.commons.players

import it.scalachikoro.commons.cards.BoardCard
import org.scalatest.funspec.AnyFunSpec

class PlayerSpec extends AnyFunSpec {
  describe("A Player") {
    val player = PlayerKoro("Test", "1", 0, Seq.empty[BoardCard])
    val more = player.receive(4)
    val less = more.give(2)
    val nothing = less._1.give(3)
    it("Doesn't have money") {
      assertResult(0)(player.money)
    }
    it("Have money") {
      assertResult(4)(more.money)
    }
    it("Give some money") {
      assertResult(2)(less._1.money)
      assertResult(2)(less._2)
    }
    it("Give all money") {
      assertResult(0)(nothing._1.money)
      assertResult(2)(nothing._2)
    }
  }
}
