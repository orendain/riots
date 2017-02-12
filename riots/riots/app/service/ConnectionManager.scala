package service

import java.util.concurrent.TimeoutException

import actor.{GuestWSActr, PlayerWSActr}
import model.UserConnection
import play.api.{Logger, Play}
import shared.{GuestID, ID, PlayerID}

import scala.concurrent.Await
import scala.concurrent.duration._

object ConnectionManager {

  private val log = Logger(this.getClass)

  implicit def UID2ID(uid: Int): ID =
    GuestIDSupervisor.isGuestID(uid) match {
      case true => GuestID(uid)
      case false => PlayerID(uid)
    }

  def isConnected(id: ID) =
    !ConnectionStore.players.get(id).isEmpty || !ConnectionStore.guests.get(id).isEmpty

  def connect(con: UserConnection) = {
    log.debug(s"UID ${con.uid} connecting.")
    GuestIDSupervisor.isGuestID(con.uid) match {
      case true => GuestWSActr.props(GuestID(con.uid), con)
      case false => PlayerWSActr.props(PlayerID(con.uid), con)
    }
  }

  def closeConnection(id: ID) =
    ActorCoordinator.system stop ConnectionStore(id).outActr

  def closeConnectionWithMessage(id: ID, msg: String) {
    try {
      val message = ChatSystem.notify(id, msg)
      val timeout = Play.current.configuration.getInt("connection.closeMessageTimeout").getOrElse(2)
      Await.ready(message, timeout seconds)
    } catch {
      case e: TimeoutException => log.debug(s"Timeout exception: $e")
    } finally {
      closeConnection(id)
    }
  }

  def disconnect(id: ID) = {
    ConnectionStore.deregister(id)
    log.debug(s"Removed ${id.id} from ConnectionStore.")
  }
}
