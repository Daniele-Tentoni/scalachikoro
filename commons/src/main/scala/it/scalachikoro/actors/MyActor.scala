package it.scalachikoro.actors

import akka.actor.Actor

abstract class MyActor extends Actor{
  protected def withRef[T](ref: Option[T])(f: T => Unit): Unit = ref match {
    case Some(value) => f(value)
    case _ =>
  }
}
