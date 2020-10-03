package it.scalachikoro.client.controllers

import akka.actor.{ActorRef, ActorSystem}
import it.scalachikoro.client.actors.GameActor
import it.scalachikoro.client.views.stages.GameStage
import it.scalachikoro.client.views.stages.scenes.SidePanelListener
import it.scalachikoro.client.views.stages.scenes.components.{BoardEventListener, BoardPanelListener, SideEventListener}
import it.scalachikoro.client.views.utils.KoroAlert
import it.scalachikoro.koro.cards.Card
import it.scalachikoro.koro.game.GameState
import it.scalachikoro.koro.players.{Player, PlayerKoro}
import it.scalachikoro.messages.GameMessages.{Acquire, Drop, EndTurn, Ready, RollDice}
import scalafx.application.{JFXApp, Platform}
import scalafx.scene.control.ButtonType

/**
 * Listener for all game events.
 */
trait GamePanelListener extends SidePanelListener with BoardPanelListener

trait GameEventListener extends SideEventListener with BoardEventListener{
  /**
   * Update all views with a new game state received from the server.
   */
  def updateGameState(state: GameState)

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
class GameController(system: ActorSystem, app: JFXApp, ref: ActorRef, state: GameState) extends Controller with GamePanelListener with GameEventListener {
  private[this] val gameStage: GameStage = GameStage(state, this)
  private[this] val gameActor: ActorRef = system.actorOf(GameActor.props("game", this, ref))
  private[this] val serverGameRef: ActorRef = ref
  serverGameRef ! Ready(gameActor)

  /**
   * @inheritdoc
   */
  override def start(): Unit = {
    Platform.runLater({
      app.stage = gameStage
    })
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
  override def receive(n: Int, from: String): Unit = gameStage receive(n, from)

  /**
   * The player must give moneys to another.
   *
   * @param n  Amount of moneys.
   * @param to Receiver.
   */
  override def give(n: Int, from: PlayerKoro, to: PlayerKoro): Unit = gameStage give(n, from, to)

  /**
   * @inheritdoc
   */
  override def acquire(card: Card): Unit = serverGameRef ! Acquire(card)

  /**
   * @inheritdoc
   */
  override def acquired(player: Player, card: Card): Unit = gameStage acquired (player, card)

  /**
   * @inheritdoc
   */
  override def playerWon(player: Player): Unit = KoroAlert.info(f"${player.name} won!", f"${player.name} have won the game!")

  /**
   * Invoke a function only if have some actor reference.
   *
   * @param f Function to invoke.
   */
  private[this] def withServerGameRef(optionRef: Option[ActorRef])(f: ActorRef => Unit): Unit = optionRef match {
    case Some(ref) => f(ref)
    case _ => println(f"No server actor.")
  }

  override def drop(): Unit = {
    val response = KoroAlert.confirmation("Drop game", "Are you sure to drop the game?") showAndWait()
    if (response.contains(ButtonType.OK)) {
      serverGameRef ! Drop()
    } else {
      KoroAlert.info("Well", "Well... Very well!") showAndWait()
    }
  }

  override def pass(): Unit = {
    serverGameRef ! EndTurn()
  }

  /**
   * Notify the view of a dice roll.
   *
   * @param n Dice result.
   */
  override def diceRolled(n: Int): Unit = gameStage diceRolled n
}