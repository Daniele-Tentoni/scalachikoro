package it.scalachikoro.client.views.stages.scenes.components

/**
 * Base trait for any Action Panel.
 */
trait ActionPanel {
  protected val defaultSpacing = 5d

  /**
   * Enable or not all controls inside the panel.
   *
   * @param b Mode.
   */
  def enable(b: Boolean)
}