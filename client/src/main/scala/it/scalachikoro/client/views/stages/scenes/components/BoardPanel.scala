package it.scalachikoro.client.views.stages.scenes.components

import it.scalachikoro.client.views.utils.KoroAlert
import it.scalachikoro.koro.cards.Card
import it.scalachikoro.koro.game.GameState.{BrokenGameState, LocalGameState}
import it.scalachikoro.koro.players.{Player, PlayerKoro}
import scalafx.geometry.Insets
import scalafx.scene.control.{Button, ButtonType, Label}
import scalafx.scene.layout.{BorderPane, HBox, VBox}

trait BoardPanel extends BorderPane with ActionPanel with BoardEventListener

trait BoardPanelListener extends CardListPanelListener {
  /**
   * The player wanna acquire a card.
   *
   * @param card Card wanna acquire.
   */
  def acquire(card: Card)
}

trait BoardEventListener {

  /**
   * The player received moneys from server.
   *
   * @param n Money received.
   */
  def receive(n: Int, from: String)

  /**
   * The player must give moneys to another.
   *
   * @param n  Amount of moneys.
   * @param to Receiver.
   */
  def give(n: Int, from: PlayerKoro, to: PlayerKoro)

  /**
   * A player have acquired a card.
   *
   * @param player Player that have done the operation.
   * @param card   Card acquired.
   */
  def acquired(player: Player, card: Card)
}

object BoardPanel {

  private[this] class BrokenBoardPanel(message: String) extends BoardPanel {
    /**
     * @inheritdoc
     */
    override def receive(n: Int, from: String): Unit = println(f"Broken: $message")

    /**
     * @inheritdoc
     */
    override def give(n: Int, from: PlayerKoro, to: PlayerKoro): Unit = println(f"Broken: $message")

    /**
     * @inheritdoc
     */
    override def acquired(player: Player, card: Card): Unit = println(f"Broken: $message")

    /**
     * @inheritdoc
     */
    override def enable(b: Boolean): Unit = println(f"Broken: $message")
  }

  private[this] class BoardPanelImpl(state: LocalGameState, listener: BoardPanelListener) extends BoardPanel {
    padding = Insets(defaultSpacing * 2)
    val otherPlayersCards: Label = Label(f"${state.others flatMap(_.name)} Cards")
    val otherPlayersCardsList: CardListPanel = CardListPanel(state.others flatMap(_.cards), listener)
    val bankCards: Label = Label("Bank Cards")
    val bankCardsList: CardListPanel = CardListPanel(state.cards, listener)
    val playerCards: Label = Label(f"${state.player.name} cards")
    val playerCardsList: CardListPanel = CardListPanel(state.player.cards, listener)

    val acquire: Button = new Button("Acquire") {
      onAction = _ => acquireConfirmation()
    }
    val commandContainer: VBox = new VBox() {
      spacing = defaultSpacing
    }
    commandContainer.children addAll acquire

    val playerContainer = new HBox()
    playerContainer.children addAll(playerCards, playerCardsList, commandContainer)
    val otherPlayerContainer = new HBox()
    playerContainer.children addAll(otherPlayersCards, otherPlayersCardsList)
    val bankContainer = new HBox()
    playerContainer.children addAll(bankCards, bankCardsList)

    top = otherPlayerContainer
    center = bankContainer
    bottom = playerContainer

    def acquireConfirmation(): Unit = {
      val result = KoroAlert.confirmation(f"You really wanna acquire", f"You really wanna acquire").showAndWait()
      if(result contains ButtonType.OK)
        println("wanna acquire") // TODO: Go with the selected card.
    }

    /**
     * @inheritdoc
     */
    override def enable(b: Boolean): Unit = println("nothing") // TODO: Enabled what have to be enabled.

    /**
     * @inheritdoc
     */
    override def receive(n: Int, from: String): Unit = KoroAlert.info(f"Received", f"Received $n from ${from}").showAndWait()

    /**
     * @inheritdoc
     */
    override def give(n: Int, from: PlayerKoro, to: PlayerKoro): Unit = KoroAlert.info(f"Given", f"Given $n from ${from.name} to ${to.name}")

    /**
     * @inheritdoc
     */
    override def acquired(player: Player, card: Card): Unit = KoroAlert.info(f"Acquired", f"Acquired $card by ${player.name}")
  }

  def apply(state: LocalGameState, listener: BoardPanelListener): BoardPanel = new BoardPanelImpl(state, listener)

  def apply(state: BrokenGameState): BoardPanel = new BrokenBoardPanel(state.message)
}