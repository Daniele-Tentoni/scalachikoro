package it.scalachikoro.messages

import akka.actor.ActorRef
import it.scalachikoro.koro.cards.Card
import it.scalachikoro.koro.game.Game
import it.scalachikoro.koro.players.PlayerRef

object LobbyMessages {

  case class Connect(name: String, ref: ActorRef)

  case class Hi(name: String)

  case class WannaQueue(name: String, ref: ActorRef)

  /**
   * Message after player is queued.
   * @param id Id given by the system.
   * @param others Number of other players in queue.
   */
  case class Queued(id: String, others: Int)

  case class Leave(id: String)

  case class LeftQueue()

}

object GameMessages {

  case class GameFound()

  case class Accept(name: String)

  case class Drop()

  case class Start(players: Seq[PlayerRef])

  case class Ready(name: String)

  case class GameState(state: Game)

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