package actor

import akka.actor.{Actor, Props}
import play.api.Logger
import service.UpdateManager

object GuestsActr extends TickableActr {
  /**
    * Create Props for a GuestsActr.
    * @return a Props for creating a GuestsActr
    */
  def props() = Props(new GuestsActr())
}

class GuestsActr extends Actor {

  val log = Logger(this.getClass)

  def receive = {
    case GuestsActr.Tick =>
      log.debug("Guests tick.")
      UpdateManager.updateGuests()
  }
}
