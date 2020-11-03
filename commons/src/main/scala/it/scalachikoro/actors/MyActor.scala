package it.scalachikoro.actors

import akka.actor.Actor

abstract class MyActor extends Actor {
  /**
   * Perform a function only if an Optional is not null.
   *
   * @param ref Optional object reference.
   * @param f   Function to execute.
   * @tparam T Type of optional.
   */
  protected def withRef[T](ref: Option[T])(f: T => Unit): Unit = ref match {
    case Some(value) => f(value)
    case _ =>
  }

  /**
   * Print in the console a formatted string.
   *
   * @param m Message to print.
   */
  protected def log(m: String): Unit = println(f"[${self.path}] $m")
}
