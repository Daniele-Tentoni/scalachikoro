package it.scalachikoro.koro.cards

import scala.language.implicitConversions

// You cannot extend this trait in other source files.
sealed trait Icon

/*
 * Declare all case classes in this file final because them cannot have implementations in other source files.
 * See https://nrinaudo.github.io/scala-best-practices/tricky_behaviours/leaky_sealed_types.html
 */
object Icon {
  // Declare Icons strings one time.
  val wheat = "Wheat"
  val cow = "Cow"
  val gear = "Gear"
  val bread = "Bread"
  val factory = "Factory"
  val fruit = "Fruit"
  val cup = "Cup"
  val major = "Major"
  val nothing = "Nothing"

  final case class Wheat() extends Icon

  final case class Cow() extends Icon

  final case class Gear() extends Icon

  final case class Bread() extends Icon

  final case class Factory() extends Icon

  final case class Fruit() extends Icon

  final case class Cup() extends Icon

  final case class Major() extends Icon

  implicit def string2icon(s: String): Icon = s match {
    case Icon.wheat => Wheat()
    case Icon.cow => Cow()
    case Icon.gear => Gear()
    case Icon.bread => Bread()
    case Icon.factory => Factory()
    case Icon.fruit => Fruit()
    case Icon.cup => Cup()
    case Icon.major => Major()
    case _ => throw new RuntimeException()
  }

  implicit def icon2string(s: Icon): String = s match {
    case Wheat() => Icon.wheat
    case Cow() => Icon.cow
    case Gear() => Icon.gear
    case Bread() => Icon.bread
    case Factory() => Icon.factory
    case Fruit() => Icon.fruit
    case Cup() => Icon.cup
    case Major() => Icon.major
    case _ => Icon.nothing // Cannot provide another implementation of Icon so don't throw new RuntimeException().
  }

}
