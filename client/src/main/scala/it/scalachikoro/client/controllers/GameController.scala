package it.scalachikoro.client.controllers

import akka.actor.ActorSystem
import it.scalachikoro.client.views.stages.GameStage
import it.scalachikoro.koro.cards.Card
import it.scalachikoro.koro.players.Player
import scalafx.application.{JFXApp, Platform}

/**
 * Listener for all game events.
 */
trait GameEventListener {
  /**
   * The player wanna roll dices.
   * @param n Number of dices.
   */
  def roll(n: Int)

  /**
   * The server have rolled the dices.
   * @param n Number rolled.
   */
  def rolled(n: Int)

  /**
   * The player received moneys from server.
   * @param n Money received.
   */
  def receive(n: Int)

  /**
   * The player wanna acquire a card.
   * @param card Card wanna acquire.
   */
  def acquire(card: Card)

  /**
   * A player have acquired a card.
   * @param player Player that have done the operation.
   * @param card Card acquired.
   */
  def acquired(player: Player, card: Card)

  /**
   * A player has won!
   * @param player Player that has won.
   */
  def playerWon(player: Player)
}

class GameController(system: ActorSystem, app: JFXApp) extends Controller with GameEventListener {
  private[this] var gameStage: GameStage = _

  /**
   * @inheritdoc
   */
  override def start(): Unit =
    Platform.runLater({
      gameStage = GameStage(this)
      // gameStage.initMatch()
      app.stage = gameStage
    })

  /**
   * @inheritdoc
   */
  override def stop(): Unit = println("GameController stopped.")

  /**
   * @inheritdoc
   */
  override def roll(n: Int): Unit = ??? // TODO: Say to the server to roll dices.

  /**
   * @inheritdoc
   */
  override def rolled(n: Int): Unit = ???

  /**
   * @inheritdoc
   */
  override def receive(n: Int): Unit = ???

  /**
   * @inheritdoc
   */
  override def acquire(card: Card): Unit = ???

  /**
   * @inheritdoc
   */
  override def acquired(player: Player, card: Card): Unit = ???

  /**
   * @inheritdoc
   */
  override def playerWon(player: Player): Unit = ???
}
