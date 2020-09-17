package it.scalachikoro.client.controllers

/**
 * Trait of any Controller with function to start and stop them.
 */
trait Controller {
  /**
   * Start the actor, eventually showing the view.
   */
  def start(): Unit

  /**
   * Stop the actor, eventually hiding the view.
   */
  def stop(): Unit
}
