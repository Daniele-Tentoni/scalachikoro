package it.scalachikoro.messages

import akka.actor.ActorRef
import it.scalachikoro.game.cards.Card
import it.scalachikoro.game.matches.Match
import it.scalachikoro.game.players.PlayerRef

object LobbyMessages {

  case class Hi(name: String, ref: ActorRef)

  case class WannaQueue(name: String)

  case class Queued(id: String)

  case class Leave(id: String)

  case class LeftQueue()

}

object GameMessages {

  case class MatchFound()

  case class Accept()

  case class Drop()

  case class Start(players: Seq[PlayerRef])

  case class Ready(name: String)

  case class GameState(state: Match)

  case class PlayerTurn()

  case class OpponentTurn(player: PlayerRef)

  case class RollDice(n: Int)

  case class DiceRolled(result: Int)

  case class Receive(n: Int)

  case class Give(n: Int, from: PlayerRef)

  case class Acquire(card: Card)

  case class Acquired(player: PlayerRef)

  case class EndTurn()

  case class PlayerWon(player: PlayerRef)

}