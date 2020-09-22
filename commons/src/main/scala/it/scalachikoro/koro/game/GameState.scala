package it.scalachikoro.koro.game

import it.scalachikoro.koro.cards.Card
import it.scalachikoro.koro.players.PlayerKoro

trait GameState

object GameState {

  case class LocalGameState(player: PlayerKoro, others: Seq[PlayerKoro], cards: Seq[Card]) extends GameState

  case class BrokenGameState(message: String) extends GameState

  def apply(game: Game, player: PlayerKoro): GameState = {
    game.players.find(_ == player).map(current => {
      LocalGameState(current, game.players.filterNot(_ == current), game.deck.visibleCards)
    }).getOrElse(BrokenGameState(f"No player with name ${player.name}"))
  }
}
