package actor

import akka.actor.{Actor, Props}
import model.event.{TeamPurchaseVote, Event}
import model.item.Item
import play.api.Logger
import service.{ItemManager, Ticker}
import shared._
import scala.collection.mutable

object TeamActr extends TickableActr {
  /**
    * Create Props for a TeamActr.
    *
    * @param id The TeamID to pass to into the actor's constructor
    * @return a Props for creating a TeamActr
    */
  def props(id: TeamID) = Props(new TeamActr(id))

  case object PlayerStatsUpdated

  case class ItemVote(playerID: PlayerID, item: Item)
  case class PlayerEventAction(eventID: Int, playerID: PlayerID, action: EventActionCommand)
}

class TeamActr(id: TeamID) extends Actor {

  val log = Logger(this.getClass)

  log.debug(s"TeamActr started for team ${id}.")

  val events = mutable.Set[Event]()

  // could traitalize if necessary
  implicit val scheduler = context.system.scheduler

  def receive = {
    case TeamActr.PlayerStatsUpdated =>
      ItemManager.recalculateTeamStats(id)
      log.debug(s"Player stats updated. Recalculating for team ${id}")
    case TeamActr.Tick => Ticker.tickTeam(id)

    case TeamActr.ItemVote(playerID, item) =>
      val ev = new TeamPurchaseVote(id, playerID, item)
      events += ev
      ev.start()

    case TeamActr.PlayerEventAction(eventID, playerID, action) =>
      events.find(_.id == eventID) match {
        case Some(ev) =>
          ev.processAction(playerID, action)
          log.debug(s"Processed.")
        case None => log.debug(s"Nooooo.")
      }
    case unhandled => log.error(s"Unhandled message received + $unhandled")
  }
}
