package actor

import akka.actor.{Actor, Props}
import play.api.Logger

object TeamsActr extends TickableActr {
  /**
    * Create Props for a TeamsActr.
    * @return a Props for creating a TeamsActr
    */
  def props() = Props(new TeamsActr())
}

class TeamsActr extends Actor {

  val log = Logger(this.getClass)

  def receive = {
    case TeamsActr.Tick =>
    case unhandled => log.error(s"Unhandled message received + $unhandled")
  }
}
