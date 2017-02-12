package model.event

import model.item.Item
import play.api.Logger
import service.{ChatSystem, CommandRelayer}
import shared._

import scala.collection.mutable
import akka.actor.Scheduler

class TeamPurchaseVote(teamID: TeamID, playerID: PlayerID, item: Item)(implicit val scheduler: Scheduler) extends Event {

  val id = 1002
  val timeLimit = 15000L
  val timeEnd = System.currentTimeMillis + timeLimit

  val playersVotes = mutable.Map[PlayerID, Boolean]()
  val passThreshhold = 51.0

  val log = Logger(this.getClass)

  def onStart() = {
    val event = EventClick2OutCmd("Vote message goes here.", "Yep", "Nope")
    CommandRelayer.send(teamID, EventStartOutCmd(id, event))

    ChatSystem.notifyAll(s"A vote has started for ${teamID}")
  }

  def processAction(id: PlayerID, action: EventActionCommand) {
    log.debug(s"Out process: ${System.currentTimeMillis} and $timeEnd")
    if (System.currentTimeMillis <= timeEnd) {
      log.debug("In process")
      action match {
        case Click1(_) =>
          playersVotes += (id -> true)
          log.debug("In click1")
        case Click2(_) => playersVotes += (id -> false)
        case _ =>
      }
    }
  }

  def onEnd() {

    val totalVotes = playersVotes.size
    val votesFor = playersVotes.count { case (_, vote) => vote == true }

    log.debug(s"for and total: $votesFor and $totalVotes")

    val ratio = votesFor * 1.0 / totalVotes * 100

    log.debug(s"ratio: $ratio")

    val msg = s"$votesFor votes out of "

    if (ratio >= passThreshhold) {
      // call onSuccessHook
      ChatSystem.notifyAll(s"Team vote passed.")
    }
    else {
      // call onFailHook? (are there more than 2 options (yes/no/dontcare)?)
      ChatSystem.notifyAll(s"Team vote failed.")
    }
  }
}
