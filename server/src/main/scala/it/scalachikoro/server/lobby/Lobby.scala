package it.scalachikoro.server.lobby

import it.scalachikoro.koro.players.PlayerRef

trait Lobby[T <: PlayerRef] {
  val items: Seq[T]

  def isEmpty: Boolean = items.isEmpty

  def players(n: Int): (Lobby[T], Option[Seq[T]])

  def +(item: T): Lobby[T]

  def -(id: String): Lobby[T]
}

case class PlayersLobby[T <: PlayerRef](override val items: Seq[T]) extends Lobby[T] {
  override def players(n: Int): (Lobby[T], Option[Seq[T]]) = if (items.size >= n)
    (copy(items = items drop n), Some(items take n))
  else
    (this, None)

  override def +(item: T): Lobby[T] = copy(items = items :+ item)

  override def -(id: String): Lobby[T] = copy(items = items filterNot (f => f.id == id))
}