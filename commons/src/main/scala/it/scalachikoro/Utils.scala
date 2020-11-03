package it.scalachikoro

import java.security.SecureRandom

import scala.collection.generic.CanBuildFrom
import scala.collection.mutable.ArrayBuffer

object Utils {
  /**
   * Get a secure random integer from Java.
   *
   * @param max max number to extract.
   * @return secure random integer.
   */
  def secureRandom(max: Int): Int = {
    val r: SecureRandom = new SecureRandom()
    val seed = r.generateSeed(32)
    r.nextBytes(seed)
    seed.hashCode() % max + 1
  }

  /**
   * Returns a new collection of the same type in a randomly chosen order.
   *
   * @return the shuffled collection.
   */
  def secureShuffle[T, CC[X] <: TraversableOnce[X]](xs: CC[T])(implicit bf: CanBuildFrom[CC[T], T, CC[T]]): CC[T] = {
    val buf = new ArrayBuffer[T] ++= xs

    def swap(i1: Int, i2: Int) {
      val tmp = buf(i1)
      buf(i1) = buf(i2)
      buf(i2) = tmp
    }

    for (n <- buf.length to 2 by -1) {
      val k = secureRandom(n)
      swap(n - 1, k)
    }

    (bf(xs) ++= buf).result()
  }
}