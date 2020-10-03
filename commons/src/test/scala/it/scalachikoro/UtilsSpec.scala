package it.scalachikoro

import org.scalacheck.Gen
import org.scalacheck.Prop.forAll
import org.scalatest.matchers.should
import org.scalatest.propspec.AnyPropSpec

class UtilsSpec
  extends AnyPropSpec
    with should.Matchers {
  val maxes: Gen[Int] = Gen.choose(1, 6)
  property("Secure random should produce random numbers") {
    forAll(maxes) { n: Int => (Utils secureRandom n) > 0 }
  }
}