package it.scalachikoro.client.views.stages.scenes.components

import it.scalachikoro.client.views.stages.scenes.SideEventListener
import scalafx.geometry.Insets
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout.{BorderPane, VBox}

trait ActionPanel {
  def enable(b: Boolean)
}

trait SidePanel extends BorderPane with ActionPanel {
  def username(name: String)

  def addHistory(progress: String)

  def timer(time: Int)
}

object SidePanel {

  private[this] class SidePanelImpl(private[this] val listener: SideEventListener) extends SidePanel {
    padding = Insets(10d)

    // TODO: Add a panel for timer

    val usernameLabel: Label = Label("Username")
    val messageLabel: Label = Label("message")
    val timerLabel: Label = Label("timer")
    val historyLabel: Label = Label("history")
    val stateContainer = new VBox()
    stateContainer.children.addAll(usernameLabel, messageLabel, timerLabel, historyLabel)
    val diceLabel: Label = Label("Dice")
    val roll1Btn: Button = new Button("Roll one dice") {
      onAction = _ => listener roll 1 // TODO: Move this behaviour to another class.
    }

    val roll2Btn: Button = new Button("Roll two dices") {
      onAction = _ => listener roll 2
    }
    val passBtn: Button = new Button("Pass") {
      onAction = _ => listener pass()
    }
    val dropBtn: Button = new Button("Drop") {
      onAction = _ => listener drop()
    }
    val btnContainer: VBox = new VBox() {
      spacing = 5d
    }
    btnContainer.children addAll(passBtn, dropBtn)
    val rightTop = new VBox()
    rightTop.children addAll stateContainer
    top = rightTop
    val rightBottom = new VBox()
    rightBottom.children addAll btnContainer
    bottom = rightBottom

    override def enable(b: Boolean): Unit = {
      passBtn.setDisable(b)
      dropBtn.setDisable(b)
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
  }

  def apply(listener: SideEventListener): SidePanel = new SidePanelImpl(listener)
}