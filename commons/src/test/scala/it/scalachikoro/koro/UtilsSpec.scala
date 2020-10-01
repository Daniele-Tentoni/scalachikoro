package it.scalachikoro.koro

import it.scalachikoro.Utils
import org.scalatest.wordspec.AnyWordSpec

class UtilsSpec extends AnyWordSpec {
  "Can get a secure random" in {
    val n = Utils.secureRandom(6)
    assert(n < 7)
    assert(n > 1)
  }
}
