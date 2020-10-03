package it.scalachikoro.server

import org.scalacheck.Gen
import org.scalacheck.Prop.forAll
import org.scalatest.matchers.should
import org.scalatest.propspec.AnyPropSpec

class IdGeneratorSpec
  extends AnyPropSpec
    with should.Matchers {
  val evenInts: Gen[Int] = for (n <- Gen choose(0, 1000)) yield n
  property("MyIdGenerator should produce unique ids") {
    var l = Set.empty[String]
    forAll(evenInts) { _: Int =>
      val id = MyIdGenerator.generateUniqueId()
      val old = l
      l = l + id
      !old.contains(id)
    }
  }
}