package actor

import akka.actor.{Actor, Props}
import model.UserConnection
import play.api.{Logger, Play}
import service._
import shared._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object PlayerWSActr {
  /**
    * Create Props for a PlayerWSActr.
    * @param id The PlayerID to pass to into the actor's constructor
    * @return a Props for creating a PlayerWSActr
    */
  def props(id: PlayerID, con: UserConnection) = Props(new PlayerWSActr(id, con))
}

class PlayerWSActr(id: PlayerID, con: UserConnection) extends Actor {

  val log = Logger(this.getClass)

  // TODO: Close duplicate connection, if any.  Reuse routine from GuestWSActr
  ConnectionStore.register(id, con)

  val player = context.actorOf(PlayerActr.props(id), s"PlayerActr-${id.id}")
  val tickFreq = Play.current.configuration.getInt("player.tickFrequency").getOrElse(5)
  context.system.scheduler.schedule(tickFreq second, tickFreq second, player, PlayerActr.Tick)

  def receive = {
    case cmd: CommandFormatter.CommandType =>
      CommandFormatter.parse(cmd) match {
        case PlayerChatInCmd(msg) => ChatSystem.playerMessage(id, msg)
        case TeamChatInCmd(msg) => ChatSystem.teamMessage(id, msg)

        // Items
        case IndividualItemInCmd(itemID) => player ! PlayerActr.UseIndividualItem(itemID)
        case TeamItemInCmd(itemID) => player ! PlayerActr.UseTeamItem(itemID)

        // Events
        case EventActionInCmd(eventID: Int, action: EventActionCommand) =>
          val data = EntityDatabase(id)
          ActorCoordinator.teamActrs(data.teamID) ! TeamActr.PlayerEventAction(eventID, id, action)
      }
    case unhandled => log.error(s"Unhandled message received + $unhandled")
  }

  override def postStop() =
    ConnectionStore.deregister(id)
}

/*
 * TODO: When checking if previous connection is dead, either 'watch' that actor
 * or use a special receive block to wait for a message from a system saying its okay
 * to enable oneself.
 */
