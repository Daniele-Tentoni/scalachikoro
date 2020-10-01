package it.scalachikoro.koro.game

import it.scalachikoro.Utils
import it.scalachikoro.koro.cards.{Card, Deck}
import it.scalachikoro.koro.players.PlayerKoro

case class Game(players: Seq[PlayerKoro], deck: Deck) {
  /**
   * A Player try to acquire a Card.
   *
   * @param card Card to acquire.
   * @param id   Id of Player.
   * @return New Game where the Player had acquired or not the Card.
   */
  def acquireCard(card: Card, id: String): Either[String, Game] =
    Either.cond(
      players.find(_.id == id).exists(_.canAcquire(card)),
      copy(
        players = players.map(m => if (m.id == id) m.acquire(card) else m),
        deck = deck.copy(cards = deck.cards filterNot (_ == card))
      ),
      "No card acquired")

  // TODO: This function have to return the new game state.
  /**
   * Apply a dice result to the actual game.
   *
   * @param n  Dice Result.
   * @param id Turn Player.
   * @return Sequence of Operation to do to synchronize game state between clients.
   */
  def applyDiceResult(n: Int, id: String): Seq[Operation] = players.find(_.id == id).map(p => {
    val incomes = p.calculateIncomes(n)
    val taxes = players.filterNot(_.id == id).flatMap(o => p.calculateTaxes(n, o))
    incomes ++ taxes
  }).getOrElse(Seq.empty)

  /**
   * Check if a Player won.
   *
   * @return True if had won, false anywhere.
   */
  def playerWon: Boolean = players.forall(_.hasWon)

}

object Game {
  /**
   * Roll many dices.
   *
   * @param n Number of dices.
   * @return Result.
   */
  def roll(n: Int): Int = {
    (0 to n).fold(0)((acc, _) => acc + Utils.getSecureRandom(6))
  }

  def apply(players: Seq[PlayerKoro]): Game = new Game(players, Deck.shuffled)
}