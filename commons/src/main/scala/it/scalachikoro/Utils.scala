package it.scalachikoro

import java.security.SecureRandom

object Utils {
  def getSecureRandom(max: Int): Int = {
    val r: SecureRandom = new SecureRandom()
    val seed = r.generateSeed(32)
    r.nextBytes(seed)
    seed.hashCode() % max + 1
  }
}
