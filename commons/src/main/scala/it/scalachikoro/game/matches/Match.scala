package it.scalachikoro.game.matches

import java.security.SecureRandom

import it.scalachikoro.game.cards.{Card, Deck}
import it.scalachikoro.game.players.PlayerKoro

import scala.util.Random

case class Match(players: Seq[PlayerKoro], deck: Deck) {
  def acquireCard(card: Card, id: String): Match = {
    val p = players.find(_.id == id).exists(_.canAcquire(card))
    if (p)
      copy(players = players.map(m => if (m.id == id) m.acquire(card) else m), deck.copy(cards = deck.cards filterNot (_ == card)))
    else
      this
  }

  private def getSecureRandom(): Int = {
    val r:SecureRandom = new SecureRandom()
    val seed = r.generateSeed(32)
    r.nextBytes(seed)
    seed.
  }

  def rollDice(n: Int, id: String): (Match, Int) = {
    val roll = (0 to n).fold(0)((acc, _) => acc + Random.nextInt(6) + 1)
    val newPlayers = players.foldLeft(Seq.empty[PlayerKoro]) { (acc, i) => {
      val inTurn = i.id == id
      val gain = i.boardCards.foldLeft(0)((acc, i) => acc + i.trigger(roll, inTurn))
      val p = i.receive(gain)
      p +: acc
    }
    }
    (copy(players = newPlayers), roll)
  }

  def playerWon: Boolean = players.forall(_.hasWon)

}

object Match {
  def apply(players: Seq[PlayerKoro]): Match = new Match(players, Deck.shuffled)
}