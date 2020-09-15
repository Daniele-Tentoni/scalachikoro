package it.scalachikoro.server.lobby

import it.scalachikoro.commons.players.Identifiable

trait Lobby[T <: Identifiable] {
  val items: Set[T]

  def isEmpty: Boolean = items.isEmpty

  def getItems(n: Int): (Lobby[T], Option[Set[T]])

  def +(item: T): Lobby[T]

  def -(id: String): Lobby[T]
}

case class PlayersLobby[T <: Identifiable](override val items: Set[T] = Set.empty) extends Lobby[T] {
  override def getItems(n: Int): (Lobby[T], Option[Set[T]]) = if (items.size >= n) {
    (copy(items = items drop n), Some(items take n))
  } else {
    (this, None)
  }

  override def +(item: T): Lobby[T] = copy(items = items + item)

  override def -(id: String): Lobby[T] = copy(items = items filterNot {
    _.id == id
  })
}