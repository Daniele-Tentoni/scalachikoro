package it.scalachikoro.client.views.stages.scenes.components

import scalafx.application.Platform
import scalafx.geometry.Pos.BottomRight
import scalafx.scene.control.{Label, ProgressIndicator}
import scalafx.scene.layout.HBox

/**
 * Provide the basic bottom bar with a message and a loading indicator.
 */
class BottomBarBox extends HBox {
  private val progress: ProgressIndicator = new ProgressIndicator()
  private val messageContainer: Label = Label("")
  alignment = BottomRight
  children.addAll(progress, messageContainer)

  /**
   * Show the loading circle in the bar.
   * @param mode True to show, False to hide.
   */
  def loading(mode: Boolean): Unit = progress.setVisible(mode)

  /**
   * Show a message in the bar label.
   * @param text Text to show.
   */
  def message(text: String): Unit = {
    Platform.runLater({
      messageContainer.setText(text)
      messageContainer.setVisible(true)
    })
  }

  /**
   * Reset all controls inside the bar.
   */
  def reset(): Unit = {
    loading(false)
    message("")
  }
}
