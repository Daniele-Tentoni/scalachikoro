package it.scalachikoro.messages

import akka.actor.ActorRef
import it.scalachikoro.koro.cards.Card
import it.scalachikoro.koro.game.GameState
import it.scalachikoro.koro.players.PlayerRef

object LobbyMessages {

  /**
   * Message from client to server to connect him.
   *
   * @param name Name of client player.
   * @param ref  Client Actor Reference.
   */
  case class Connect(name: String, ref: ActorRef)

  /**
   * Message from Server to Client to confirm connection.
   *
   * @param name Name of Server.
   */
  case class Hi(name: String)

  /**
   * Message from client that wanna queue a game.
   *
   * @param name Name of the player.
   * @param ref  Player actor reference.
   */
  case class WannaQueue(name: String, ref: ActorRef)

  /**
   * Message after player is queued.
   *
   * @param id     Id given by the system.
   * @param others Number of other players in queue.
   */
  case class Queued(id: String, others: Int)

  /**
   * Message Client -> Server to leave the queue.
   *
   * @param id Id of Client Actor.
   */
  case class Leave(id: String)

  /**
   * Message Server -> Client that confirm the left.
   */
  case class LeftQueue()

  /**
   * Message Lobby -> Game to start a new Game.
   *
   * @param players Sequence of PlayerRef selected.
   */
  case class Start(players: Seq[PlayerRef])

}

object GameMessages {

  /**
   * Message Server -> Client to invite him to a Game.
   */
  case class GameInvitation()

  /**
   * Message Client -> Server that accept the Invite
   *
   * @param name Name of Acceptor.
   */
  case class Accept(name: String)

  /**
   * Message Client -> Server that decline the Invite.
   *
   * @param name Name of Decliner.
   */
  case class Decline(name: String)

  /**
   * Message Client -> Server to leave a Game.
   */
  case class Drop()

  /**
   * (Client -> Game) Say that is ready to start to play.
   *
   * @param ref Name of local Player.
   */
  case class Ready(ref: ActorRef)

  /**
   * Message Game -> Client to notify the current Game.
   *
   * @param ref   Remote Game ActorRef.
   * @param state Current Game.
   */
  case class UpdateState(ref: ActorRef, state: GameState)

  /**
   * Message Game -> Client to notify the start of Turn.
   */
  case class PlayerTurn()

  /**
   * Message Game -> Client to notify the start of another Player Turn.
   *
   * @param player Other player.
   */
  case class OpponentTurn(player: PlayerRef)

  case class RollDice(n: Int)

  /**
   * A Player have rolled dices.
   *
   * @param result Result of roll.
   * @param ref    Player Reference.
   */
  case class DiceRolled(result: Int, ref: PlayerRef)

  case class Receive(n: Int, from: String)

  case class Give(n: Int, to: String)

  case class Acquire(card: Card)

  case class Acquired(card: Card, player: PlayerRef)

  case class NotAcquired(message: String)

  case class EndTurn()

  case class PlayerWon(player: PlayerRef)

}