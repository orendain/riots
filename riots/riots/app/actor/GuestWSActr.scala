package actor

import akka.actor.{Actor, Props}
import model.UserConnection
import play.api.{Logger, Play}
import service.{CommandFormatter, ChatSystem, ConnectionManager, ConnectionStore}
import shared._

object GuestWSActr {
  /**
    * Create Props for a GuestWSActr.
    * @param id The GuestID to pass to into the actor's constructor
    * @return a Props for creating a GuestWSActr
    */
  def props(id: GuestID, con: UserConnection) = Props(new GuestWSActr(id, con))
}

class GuestWSActr(id: GuestID, con: UserConnection) extends Actor {

  val log = Logger(this.getClass)
  log.debug(s"GuestWSActr for ${id.id} being initialized.")

  if (ConnectionManager.isConnected(id)) {
    log.debug(s"Closing duplicate connection for guest ${id.id}.")
    val closeMsg = Play.current.configuration.getString("connection.duplicateConnectionClosedMessage").getOrElse("duplicateConnectionClosedMessage")
    ConnectionManager.closeConnectionWithMessage(id, closeMsg)
  }

  // TODO: Don't need to wait for previous connection to close,
  // but it would be nice to wait for it to deregister so we
  // know we have cleaned up.
  // TODO: Guest UIDs should expire periodically from user's front ends.
  ConnectionStore.register(id, con)

  def receive = {
    case cmd: CommandFormatter.CommandType =>
      CommandFormatter.parse(cmd) match {
        case PlayerChatInCmd(msg) => ChatSystem.guestMessage(id, msg)
        case _ =>
      }
    case _ => log.error("Unhandled message received.")
  }

  override def postStop() =
    ConnectionStore.deregister(id)
}
