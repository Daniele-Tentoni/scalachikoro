package it.scalachikoro.matchs

import it.scalachikoro.commons.cards.{Card, Deck}
import it.scalachikoro.commons.players.{Player, PlayerKoro}

import scala.util.Random

case class Match(players: Seq[PlayerKoro], deck: Deck) {
  def acquireCard(): Match = ???

  def rollDice(n: Int, turnPlayer: PlayerKoro): (Match, Int) = {
    val roll = (0 to n).fold(0)((acc, _) => acc + Random.nextInt(6) + 1)
    val newPlayers = players.foldLeft(Seq.empty[PlayerKoro]) { (acc, i) => {
      val inTurn = i == turnPlayer
      val gain = i.cards.foldLeft(0)((acc, i) => acc + i.trigger(roll, inTurn))
      val p = i.receive(gain)
      p +: acc
    }}
    (copy(players = newPlayers), roll)
  }

}

object Match {
  def apply(players: Seq[Player]): Match = new Match(
    players.map(m => PlayerKoro(m.name, m.id, 0, Seq.empty[Card])),
    Deck.shuffled
  )
}