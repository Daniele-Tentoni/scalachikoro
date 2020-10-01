package it.scalachikoro.client.controllers

import akka.actor.{ActorRef, ActorSystem}
import it.scalachikoro.client.actors.GameActor
import it.scalachikoro.client.views.stages.GameStage
import it.scalachikoro.koro.cards.Card
import it.scalachikoro.koro.game.GameState
import it.scalachikoro.koro.players.Player
import it.scalachikoro.messages.GameMessages.RollDice
import scalafx.application.{JFXApp, Platform}

/**
 * Listener for all game events.
 */
trait GameEventListener {
  /**
   * Update all views with a new game state received from the server.
   */
  def updateGameState(ref: ActorRef, state: GameState)

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
class GameController(system: ActorSystem, app: JFXApp) extends Controller with GameEventListener {
  private[this] val gameStage: GameStage = GameStage(this)
  private[this] var gameActor: Option[ActorRef] = None
  var serverGameRef: Option[ActorRef] = None

  /**
   * @inheritdoc
   */
  override def start(): Unit = {
    Platform.runLater({ app.stage = gameStage })
    println("GameController started.")
  }

  override def updateGameState(ref: ActorRef, state: GameState): Unit = {
    println("New game state received.")
    serverGameRef = Some(ref)
    gameActor = Some(system.actorOf(GameActor.props("game", this, ref)))
    gameStage.updateGameState(state)
  }

  /**
   * @inheritdoc
   */
  override def stop(): Unit = println("GameController stopped.")

  /**
   * @inheritdoc
   */
  override def roll(n: Int): Unit = withServerGameRef {
    _ ! RollDice(n)
  }

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
  private[this] def withServerGameRef(f: ActorRef => Unit): Unit = {
    serverGameRef match {
      case Some(ref) => f(ref)
      case _ => println(f"No server actor.")
    }
  }
}