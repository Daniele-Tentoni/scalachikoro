package it.scalachikoro.koro.players

import it.scalachikoro.koro.cards.{AimCard, Card}
import it.scalachikoro.koro.cards.CardType.PrimaryIndustry
import it.scalachikoro.koro.cards.Icon.Wheat
import org.scalatest.wordspec.AnyWordSpec

class PlayerKoroSpec extends AnyWordSpec {
  val testName = "Test"

  "A PlayerKoro" when {
    val p = PlayerKoro.init("1", testName)
    "at Start" should {
      "receive starter cards" in {
        assertResult(Card.starterCards)(p.cards)
      }
    }
    "in game" should {
      val c = Card(testName, PrimaryIndustry(Seq(1)), Wheat(), 1, 1, 1)
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
        assert(q.cards.contains(c))
      }
      "don't acquire if have max copies" in {
        val q = p.receive(1).acquire(c)
        assert(!q.canAcquire(c))
      }
      "have won if have all aim cards" in {
        assert(!p.hasWon)
        val w = PlayerKoro(testName, testName, 0, Card(testName, PrimaryIndustry(Seq(1)), Wheat(), 1, 1, 1) +: AimCard.all)
        assert(w.hasWon)
      }
    }
  }

  "A Bank PlayerKoro" when {
    "istanziated" should {
      val b = PlayerKoro.bank
      "Have default values" in {
        assertResult(PlayerKoro.bankName)(b.id)
        assertResult(PlayerKoro.bankName)(b.name)
        assertResult(999)(b.money)
      }
      "Have no cards" in {
        assert(b.cards.isEmpty)
      }
    }
  }
}
