package it.scalachikoro.matchs

import it.scalachikoro.cards.Deck
import it.scalachikoro.players.{Player, PlayerKoro}

case class Match(players: Seq[PlayerKoro], deck: Deck) {
}

object Match {
  def apply(players: Seq[Player]): Match = new Match(
    players.map(m => PlayerKoro(m.name, m.id, Seq.empty)),
    Deck.shuffled
  )
}