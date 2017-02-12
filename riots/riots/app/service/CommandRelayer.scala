package service

import model.UserConnection
import play.api.Logger
import shared._

object CommandRelayer {

  val log = Logger(this.getClass)

  def send(id: ID, cmd: Command) {
    id match {
      case i: PlayerID => sendToPlayer(i, cmd)
      case i: GuestID => sendToPlayer(i, cmd)
      case i: TeamID => sendToTeam(i, cmd)
      case _ =>
    }
  }

  def sendToAll(cmd: Command) {
    sendToPlayers(cmd)
    sendToGuests(cmd)
  }

  def sendToPlayers(cmd: Command) {
    val json = CommandFormatter.build(cmd)
    ConnectionStore.players.values foreach { case UserConnection(id, out) => out ! json }
  }

  def sendToGuests(cmd: Command) {
    val json = CommandFormatter.build(cmd)
    log.debug(s"Preparing to send command to guests: $cmd")
    ConnectionStore.guests.values foreach {
      case UserConnection(uid, out) =>
        log.debug(s"Sending command to guest $uid: $cmd")
        out ! json
    }
  }

  private def sendToPlayer(id: ID, cmd: Command) {
    ConnectionStore(id).outActr ! CommandFormatter.build(cmd)
  }

  private def sendToTeam(id: TeamID, cmd: Command) {
    val json = CommandFormatter.build(cmd)
    ConnectionStore.players foreach { case (uid, con) =>
      if (EntityDatabase(uid).teamID == id)
        con.outActr ! json
    }
  }
}
