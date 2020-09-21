package it.scalachikoro.koro.game

import java.security.SecureRandom

import it.scalachikoro.koro.cards.{Card, Deck}
import it.scalachikoro.koro.players.PlayerKoro

case class Game(players: Seq[PlayerKoro], deck: Deck) {
  def acquireCard(card: Card, id: String): Game = {
    val p = players.find(_.id == id).exists(_.canAcquire(card))
    if (p)
      copy(players = players.map(m => if (m.id == id) m.acquire(card) else m), deck.copy(cards = deck.cards filterNot (_ == card)))
    else
      this
  }

  def applyDiceResult(n: Int, id: String): Seq[PlayerKoro] = {
    players.foldLeft(Seq.empty[PlayerKoro]) { (acc, i) => {
      // Get if the player is in turn or not.
      val inTurn = i.id == id
      // Calculate all rewards for this player.
      val gain = i.boardCards.foldLeft(0)((acc, i) => acc + i.trigger(n, inTurn))
      // Give to a player his gain.
      val p = i.receive(gain)
      p +: acc
    }
    }
  }

  def playerWon: Boolean = players.forall(_.hasWon)

}

object Game {

  protected def getSecureRandom: Int = {
    val r: SecureRandom = new SecureRandom()
    val seed = r.generateSeed(32)
    r.nextBytes(seed)
    seed.hashCode() % 6 + 1
  }

  def rollDice(n: Int): Int = {
    (0 to n).fold(0)((acc, _) => acc + getSecureRandom)
  }

  def apply(players: Seq[PlayerKoro]): Game = new Game(players, Deck.shuffled)
}