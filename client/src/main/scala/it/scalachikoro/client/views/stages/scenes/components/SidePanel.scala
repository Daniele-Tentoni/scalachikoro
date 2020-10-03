package it.scalachikoro.client.views.stages.scenes.components

import it.scalachikoro.client.views.stages.scenes.SidePanelListener
import scalafx.geometry.Insets
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout.{BorderPane, VBox}

trait SidePanel extends BorderPane with ActionPanel with SideEventListener {
  def username(name: String)

  def addHistory(progress: String)

  def timer(time: Int)
}

trait SideEventListener extends DiceEventListener

object SidePanel {

  private[this] class SidePanelImpl(listener: SidePanelListener) extends SidePanel {
    padding = Insets(defaultSpacing * 2)

    // TODO: Add a panel for timer

    val usernameLabel: Label = Label("username")
    val messageLabel: Label = Label("message")
    val timerLabel: Label = Label("timer")
    val historyLabel: Label = Label("history")
    val stateContainer: VBox = new VBox() {
      spacing = defaultSpacing
    }
    stateContainer.children addAll(usernameLabel, messageLabel, timerLabel, historyLabel)

    val passBtn: Button = new Button("Pass") {
      onAction = _ => listener pass()
    }
    val dropBtn: Button = new Button("Drop") {
      onAction = _ => listener drop()
    }
    val btnContainer: VBox = new VBox() {
      spacing = defaultSpacing
    }
    btnContainer.children addAll(passBtn, dropBtn)

    val rightTop = new VBox()
    rightTop.children addAll stateContainer
    top = rightTop
    val dicePanel: DicePanel = DicePanel(listener)
    center = dicePanel
    val rightBottom = new VBox()
    rightBottom.children addAll btnContainer
    bottom = rightBottom

    override def enable(b: Boolean): Unit = {
      passBtn setDisable b
      dropBtn setDisable b
    }

    override def username(name: String): Unit = usernameLabel.text = name

    // TODO: We can move those in another panel.
    private[this] var history = ""

    override def addHistory(progress: String): Unit = {
      history = f"$history\n$progress"
      historyLabel.text = history
    }

    private[this] var timer = 0

    override def timer(time: Int): Unit = {
      timer = time // TODO: When time reach 0, notify an event.
    }

    /**
     * Pass to another listener.
     *
     * @param n Dice result.
     */
    override def diceRolled(n: Int): Unit = dicePanel.diceRolled(n)
  }

  def apply(listener: SidePanelListener): SidePanel = new SidePanelImpl(listener)
}