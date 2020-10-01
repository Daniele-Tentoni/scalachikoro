package it.scalachikoro.koro.game

import it.scalachikoro.koro.players.PlayerKoro

sealed class Operation

object Operation{

  /**
   * Give Money operation.
   * @param amount Amount of moneys.
   * @param to Receiver.
   */
  case class Give(amount: Int, to: PlayerKoro) extends Operation

  /**
   * Receive Money operation.
   * @param amount Amount of moneys.
   * @param from Giver.
   */
  case class Receive(amount: Int, from: PlayerKoro) extends Operation

  /**
   * No Operation needed.
   */
  case object NoOperation extends Operation
}
