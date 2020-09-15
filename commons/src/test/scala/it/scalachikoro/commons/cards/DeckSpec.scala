package it.scalachikoro.commons.cards

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class DeckSpec extends AnyWordSpec with Matchers {
  "A Deck" when {
    "when created" should {
      "have all cards" in {
        assertResult(Deck(BoardCard.allBoardCards))(Deck.sorted)
      }
      "is not empty" in {
        assertResult(84)(Deck.sorted.remaining)
        assert(!Deck.sorted.isEmpty)
      }
      "have visible cards" in {
        assertResult(BoardCard.allBoardCards take 15)(Deck.sorted.visibleCards)
      }
      "pick the first card" in {
        assertResult(BoardCard.allBoardCards.headOption)(Deck.sorted.pickCard(1)._2)
      }
    }
  }
}
