package service

import model.UserConnection
import play.api.Logger
import shared.{GuestID, ID, PlayerID}

import scala.collection.mutable

/*
 * TODO: Consider keeping all connections in a single collection for reduced pattern matching.
 * def players/guests will need to filter list before return, which can be
 * optimized by caching it after every add/remove.  Or actually just every
 * call to get players/guests (lazy approach).
 */
object ConnectionStore {

  val log = Logger(this.getClass)

  private val playerList = mutable.Map[ID, UserConnection]()
  private val guestList = mutable.Map[ID, UserConnection]()

  def players = playerList.toMap
  def guests = guestList.toMap

  def apply(id: ID) =
    id match {
      case PlayerID(uid) => playerList(id)
      case GuestID(uid) => guestList(id)
    }

  def register(id: ID, con: UserConnection) {
    id match {
      case PlayerID(uid) => playerList += (id -> con)
      case GuestID(uid) => guestList += (id -> con)
    }
    log.debug(s"Registered ${id.id} with ConnectionStore.")
  }

  def deregister(id: ID) {
    id match {
      case PlayerID(uid) => playerList -= id
      case GuestID(uid) => guestList -= id
    }
    log.debug(s"Deregistered ${id.id} with ConnectionStore.")
  }
}
