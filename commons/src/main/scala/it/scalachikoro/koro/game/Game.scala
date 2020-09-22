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

  def applyDiceResult(n: Int, id: String): Seq[Operation] = players.find(_.id == id).map(p => {
    val incomes = p.calculateIncomes(n)
    val taxes = players.filterNot(_.id == id).flatMap(o => p.calculateTaxes(n, o))
    incomes ++ taxes
  }).getOrElse(Seq.empty)

  def playerWon: Boolean = players.forall(_.hasWon)

}

object Game {

  protected def getSecureRandom: Int = {
    val r: SecureRandom = new SecureRandom()
    val seed = r.generateSeed(32)
    r.nextBytes(seed)
    seed.hashCode() % 6 + 1
  }

  /**
   * Roll many dices.
   *
   * @param n Number of dices.
   * @return Result.
   */
  def roll(n: Int): Int = {
    (0 to n).fold(0)((acc, _) => acc + getSecureRandom)
  }

  def apply(players: Seq[PlayerKoro]): Game = new Game(players, Deck.shuffled)
}