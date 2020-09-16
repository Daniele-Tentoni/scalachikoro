package it.scalachikoro.game.players

import it.scalachikoro.game.cards.Card
import it.scalachikoro.game.cards.CardType.PrimaryIndustry
import org.scalatest.wordspec.AnyWordSpec

class PlayerKoroSpec extends AnyWordSpec {
  "A PlayerKoro" when {
    "is in turn" should {
      "buy cards" in {
        val p = PlayerKoro("1", "Test", 0, Seq.empty)
        val c = Card("Test", 1, PrimaryIndustry(Seq(1)), 1)
        assert(!p.canAcquire(c))
        val richP = PlayerKoro("2", "Test 2", 1, Seq.empty)
        assert(richP.canAcquire(c))
        val moreRichP = PlayerKoro("2", "Test 2", 2, Seq.empty)
        assert(moreRichP.canAcquire(c))
      }
    }
  }
}
