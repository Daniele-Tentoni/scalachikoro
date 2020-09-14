package it.scalachikoro.messages

object LobbyMessages {
  case class Hi(name: String)
  case class Queued(pos: Int)
  case class Leave()
  case class LeftQueue()
}
