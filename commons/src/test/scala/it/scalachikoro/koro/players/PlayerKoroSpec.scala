package it.scalachikoro.koro.players

import it.scalachikoro.koro.cards.{AimCard, Card}
import it.scalachikoro.koro.cards.CardType.PrimaryIndustry
import org.scalatest.wordspec.AnyWordSpec

class PlayerKoroSpec extends AnyWordSpec {
  "A PlayerKoro" when {
    val p = PlayerKoro.init("1", "Test")
    "at Start" should {
      "receive starter cards" in {
        assertResult(Card.starterCards)(p.boardCards)
      }
    }
    "in game" should {
      val c = Card("Test", 1, PrimaryIndustry(Seq(1)), 1)
      "not buy a card if haven't much moneys" in {
        assert(!p.canAcquire(c))
      }
      "buy a card if have just moneys" in {
        val q = p.receive(1)
        assert(q.canAcquire(c))
      }
      "buy a card have more money than needed" in {
        val q = p.receive(2)
        assert(q.canAcquire(c))
      }
      "acquire a card" in {
        val q = p.receive(1).acquire(c)
        assert(q.boardCards.contains(c))
      }
      "don't acquire if have max copies" in {
        val q = p.receive(1).acquire(c)
        assert(!q.canAcquire(c))
      }
      "have won if have all aim cards" in {
        assert(!p.hasWon)
        val w = PlayerKoro("Test", "Test", 0, Card("Test", 1, PrimaryIndustry(Seq(1)), 1) +: AimCard.all)
        assert(w.hasWon)
      }
    }
  }
}
