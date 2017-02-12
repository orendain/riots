package actor

import akka.actor.{Actor, Props}
import play.api.Logger
import service._
import shared._

object PlayerActr extends TickableActr {
  /**
    * Create Props for a PlayerActr.
    * @param id The PlayerID to pass to into the actor's constructor
    * @return a Props for creating a PlayerActr
    */
  def props(id: PlayerID) = Props(new PlayerActr(id))

  case class UseIndividualItem(itemID: Int)
  case class UseTeamItem(itemID: Int)
}

class PlayerActr(id: PlayerID) extends Actor {

  val log = Logger(this.getClass)

  // TODO: Probably belongs here, but group all necessary functions into a neat little init routine.
  val data = EntityDatabase(id)
  ItemManager.recalculateStats(data)
  UpdateManager.markChanged(id)
  UpdateManager.update(id)
  UpdateManager.updateItems(id, data.enabled)

  def receive = {
    case PlayerActr.UseIndividualItem(itemID) => ItemManager.useIndividualItem(id, itemID)
    case PlayerActr.UseTeamItem(itemID) => ItemManager.useTeamItem(id, itemID)

    case PlayerActr.Tick => Ticker.tickPlayer(id)

    case unhandled => log.error(s"Unhandled message received + $unhandled")
  }

  override def postStop() {
    log.debug(s"postStop for player ${id.id}")
  }
}
