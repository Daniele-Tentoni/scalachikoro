package it.scalachikoro.server

import java.util.UUID

import scala.annotation.tailrec

trait IdGenerator {
  def generateId(): String
}

object MyIdGenerator extends IdGenerator {
  var ids = Set.empty[String]

  override def generateId(): String = UUID randomUUID() toString()

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
