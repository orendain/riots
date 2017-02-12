package service

import play.api.Logger
import shared._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object ChatSystem {

  val log = Logger(this.getClass)

  // TODO: notification type enumeration
  def notify(id: ID, msg: String, cls: String = "not") = Future {
    CommandRelayer.send(id, NotificationChatOutCmd(msg, cls))
    log.debug(s"Sent notification to ${id.id}.")
  }

  def notifyAll(msg: String, cls: String = "not") = Future {
    CommandRelayer.sendToAll(NotificationChatOutCmd(msg, cls))
  }

  def playerMessage(id: PlayerID, msg: String) = Future {
    val data = EntityDatabase(id)
    CommandRelayer.sendToAll(PlayerChatOutCmd(msg, data.name, "t_" + data.teamID))
  }

  def teamMessage(id: PlayerID, msg: String) = Future {
    val data = EntityDatabase(id)
    CommandRelayer.send(data.teamID, TeamChatOutCmd(msg, data.name, "t_" + data.teamID))
  }

  def guestMessage(id: GuestID, msg: String) = Future {
    log.debug(s"Attempting to send guest ${id.id} msg: $msg")
    CommandRelayer.sendToAll(PlayerChatOutCmd(msg, "Guest", "t_g"))
  }
}
