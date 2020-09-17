package it.scalachikoro.client.controllers

import akka.actor.ActorSystem
import it.scalachikoro.client.views.stages.GameStage
import it.scalachikoro.koro.cards.Card
import it.scalachikoro.koro.players.Player
import scalafx.application.{JFXApp, Platform}

trait GameEventListener {
  def rolled(n: Int)

  def acquired(player: Player, card: Card)

  def playerWon(player: Player)
}

// TODO: Document this.
class GameController(system: ActorSystem, app: JFXApp) extends Controller with GameEventListener {
  private var gameStage: GameStage = _

  override def start(): Unit =
    Platform.runLater({
      gameStage = GameStage(this)
      // gameStage.initMatch()
      app.stage = gameStage
    })

  override def stop(): Unit = println("GameController stopped.")

  override def rolled(n: Int): Unit = ???

  override def acquired(player: Player, card: Card): Unit = ???

  override def playerWon(player: Player): Unit = ???
}
