package it.scalachikoro.client.controllers.listeners

import akka.actor.ActorRef
import it.scalachikoro.koro.game.GameState

trait MainViewActorListener {
  /**
   * Say Hi to the server actor.
   *
   * @param name Name to present to the server.
   */
  def connect(name: String, server: String, port: String)

  /**
   * Return the response of an Hi message.
   *
   * @param name   Name received.
   * @param server Get the correct server reference.
   */
  def welcomed(name: String, server: ActorRef)

  /**
   * Say to server to queue the player.
   *
   * @param name Name of the player to queue.
   */
  def queue(name: String)

  /**
   * Return the response of the queue message.
   *
   * @param id     Id of player given by the system.
   * @param name   Name of player queued.
   * @param others Number of other players.
   */
  def queued(id: String, name: String, others: Int)

  /**
   * Say to server to leave the queue.
   */
  def leaveQueue()

  /**
   * Return the response of a queue left.
   */
  def queueLeft(name: String)

  /**
   * Return the response of a match found.
   */
  def matchFound(name: String, gameRef: ActorRef)

  /**
   * Say to server the response to the game call.
   *
   * @param response Response to the game call.
   */
  def inviteAccepted(name: String, response: Boolean, gameRef: ActorRef)

  /**
   * The Server says that a Game is started.
   */
  def gameStarted(ref: ActorRef, state: GameState)
}
