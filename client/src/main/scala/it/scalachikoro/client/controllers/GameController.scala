package it.scalachikoro.client.controllers

import akka.actor.{ActorRef, ActorSystem}
import it.scalachikoro.client.actors.GameActor
import it.scalachikoro.client.views.stages.GameStage
import it.scalachikoro.koro.cards.Card
import it.scalachikoro.koro.game.GameState
import it.scalachikoro.koro.players.Player
import it.scalachikoro.messages.GameMessages.{Ready, RollDice}
import scalafx.application.{JFXApp, Platform}

/**
 * Listener for all game events.
 */
trait GameEventListener {
  /**
   * Update all views with a new game state received from the server.
   */
  def updateGameState(state: GameState)

  /**
   * The player wanna roll dices.
   *
   * @param n Number of dices.
   */
  def roll(n: Int)

  /**
   * The server have rolled the dices.
   *
   * @param n Number rolled.
   */
  def rolled(n: Int)

  /**
   * The player received moneys from server.
   *
   * @param n Money received.
   */
  def receive(n: Int)

  /**
   * The player wanna acquire a card.
   *
   * @param card Card wanna acquire.
   */
  def acquire(card: Card)

  /**
   * A player have acquired a card.
   *
   * @param player Player that have done the operation.
   * @param card   Card acquired.
   */
  def acquired(player: Player, card: Card)

  /**
   * A player has won!
   *
   * @param player Player that has won.
   */
  def playerWon(player: Player)
}

/**
 * Controller for all game business logic and game views.
 *
 * @param system The Singleton Actor System.
 * @param app    The Singleton JFXApp.
 */
class GameController(system: ActorSystem, app: JFXApp, ref: ActorRef, state: GameState) extends Controller with GameEventListener {
  private[this] val gameStage: GameStage = GameStage(state, this)
  private[this] val gameActor: ActorRef = system.actorOf(GameActor.props("game", this, ref))
  private[this] val serverGameRef: ActorRef = ref
  serverGameRef ! Ready(gameActor)

  /**
   * @inheritdoc
   */
  override def start(): Unit = {
    Platform.runLater({ app.stage = gameStage })
    println("GameController started.")
  }

  override def updateGameState(state: GameState): Unit = {
    println("New game state received.")
    gameStage.updateGameState(state)
  }

  /**
   * @inheritdoc
   */
  override def stop(): Unit = println("GameController stopped.")

  /**
   * @inheritdoc
   */
  override def roll(n: Int): Unit = serverGameRef ! RollDice(n)

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

  /**
   * Invoke a function only if have some actor reference.
   *
   * @param f Function to invoke.
   */
  private[this] def withServerGameRef(optionRef: Option[ActorRef])(f: ActorRef => Unit): Unit = optionRef match {
      case Some(ref) => f(ref)
      case _ => println(f"No server actor.")
    }
}