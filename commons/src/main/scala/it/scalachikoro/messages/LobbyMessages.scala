package it.scalachikoro.messages

object LobbyMessages {

  case class Hi(name: String)

  case class WannaQueue(name: String)

  case class Queued(id: String)

  case class Leave(id: String)

  case class LeftQueue()

}
