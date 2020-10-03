package it.scalachikoro.server

import java.util.UUID

import scala.annotation.tailrec

trait IdGenerator {
  def generateId(): String = UUID randomUUID() toString()
}

object MyIdGenerator extends IdGenerator {
  var ids = Set.empty[String]

  @tailrec
  def generateUniqueId(): String = {
    val id = generateId()
    if (ids.contains(id))
      generateUniqueId()
    else {
      ids = ids + id
      id
    }
  }
}
