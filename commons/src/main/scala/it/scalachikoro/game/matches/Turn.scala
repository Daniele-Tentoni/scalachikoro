package it.scalachikoro.game.matches

trait Turn[T] {
  def getTurn: T

  def nextTurn: T

  def all: Seq[T]
}

object Turn {
  def apply[T](items: Seq[T]): Turn[T] = new TurnImpl[T](items)

  private class TurnImpl[T](items: Seq[T]) extends Turn[T] {
    private var index = 0

    override def getTurn: T = items(index)

    override def nextTurn: T = {
      index = (index + 1) % items.size
      getTurn
    }

    override def all: Seq[T] = items
  }

}
