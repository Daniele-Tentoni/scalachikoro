package it.scalachikoro.koro

import it.scalachikoro.Utils
import org.scalacheck.Gen
import org.scalacheck.Prop.forAll
import org.scalatest.matchers.should
import org.scalatest.propspec.AnyPropSpec

class UtilsSpec
  extends AnyPropSpec
    with should.Matchers {
  val evenInts: Gen[Int] = for (n <- Gen choose(0, 1000)) yield n
  property("MyIdGenerator should produce unique ids") {
    forAll(evenInts) { n: Int =>
      val r = Utils.secureRandom(n)
      r  should be
      r should be
      assert(n < n)
      assert(n > 0)
    }
  }
}