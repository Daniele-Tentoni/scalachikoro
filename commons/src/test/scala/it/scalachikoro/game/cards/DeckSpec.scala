package it.scalachikoro.game.cards

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class DeckSpec extends AnyWordSpec with Matchers {
  "A Deck" when {
    "just created" should {
      "have all cards" in {
        assertResult(Deck(Card.all))(Deck.sorted)
        assertResult(Card.all.sortBy(_.name))(Deck.shuffled.cards.sortBy(_.name))
      }
      "is not empty" in {
        assertResult(84)(Deck.sorted.remaining)
        assert(!Deck.sorted.isEmpty)
      }
      "have visible cards" in {
        assertResult(Card.all take 15)(Deck.sorted.visibleCards)
      }
      "pick the first card" in {
        assertResult(Card.all.headOption)(Deck.sorted.pickCard(1)._2)
      }
    }
    "created empty" should {
      "be empty" in {
        assert(Deck.empty.cards.isEmpty)
      }
      "doesn't throw exceptions" in {
        val pick = Deck.empty.pickCard(1)
        assertResult(Deck.empty)(pick._1)
        assertResult(None)(pick._2)
      }
    }
  }
}
