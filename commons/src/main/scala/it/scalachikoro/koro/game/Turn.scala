package it.scalachikoro.koro.game

trait Turn[T] {
  def actual: T

  def next: T

  def all: Seq[T]
}

object Turn {
  def apply[T](items: Seq[T]): Turn[T] = new TurnImpl[T](items)

  private[this] class TurnImpl[T](items: Seq[T]) extends Turn[T] {
    private[this] var index = 0

    override def actual: T = items(index)

    override def next: T = {
      index = (index + 1) % items.size
      actual
    }

    override def all: Seq[T] = items
  }

}
