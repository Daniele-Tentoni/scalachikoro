package it.scalachikoro.koro.game

trait Turn[T] {
  def get: T

  def next: T

  def all: Seq[T]
}

object Turn {
  def apply[T](items: Seq[T]): Turn[T] = new TurnImpl[T](items)

  private class TurnImpl[T](items: Seq[T]) extends Turn[T] {
    private var index = 0

    override def get: T = items(index)

    override def next: T = {
      index = (index + 1) % items.size
      get
    }

    override def all: Seq[T] = items
  }

}
