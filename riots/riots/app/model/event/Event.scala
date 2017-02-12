package model.event

import service.{ChatSystem, EntityDatabase, CommandRelayer}
import shared._
import scala.collection.mutable

import akka.actor.Scheduler
import scala.concurrent.duration._

import scala.concurrent.ExecutionContext.Implicits.global
// OR use the akka's system's dispatcher as ExecutionContext
//import system.dispatcher (would need to pass in system)

trait Event {

  val id: Int
  val timeLimit: Long
  val timeEnd: Long

  val scheduler: Scheduler

  def start() {
    onStart()
    scheduler.scheduleOnce(timeLimit milliseconds) {
      onEnd()
    }
  }

  def onStart()
  def processAction(id: PlayerID, action: EventActionCommand)
  def onEnd()
}


class PowerGrab()(implicit val scheduler: Scheduler) extends Event {

  val id = 1001
  val timeLimit = 30000L
  val timeEnd = System.currentTimeMillis + timeLimit

  val playersClicked = mutable.Map[PlayerID, Long]()

  def onStart() = {
    val event = EventClick1OptionOutCmd("A wild pile of brains appears. Quick, grab a handful!", "GRAB!")
    CommandRelayer.sendToPlayers(EventStartOutCmd(id, event))

    ChatSystem.notifyAll("A global event has begun! Hold onto your panties!")
  }

  def processAction(id: PlayerID, action: EventActionCommand) {
    if (System.currentTimeMillis <= timeEnd) {
      action match {
        case Click1(powerUsed) => playersClicked += (id -> powerUsed)
        case _ =>
      }
    }
  }

  def onEnd() {
    val teamTotals = mutable.Map[TeamID, Long]()

    // TODO: either check teamID at player click time
    // or dissalow players from switching teams mid-events
    playersClicked foreach { case (id, power) =>
      val teamID = EntityDatabase(id).teamID
      teamTotals.update(teamID, teamTotals(teamID) + power + 1)
    }

    // TODO: ties currently go to a random team ... not cool!
    var winningScore = (teamTotals.head._1, -1L)
    teamTotals.foreach { case (id, total) =>
      if (total > winningScore._2)
        winningScore = (id, total)
    }

    // TODO: more descriptive obviously, plus award prizes
    ChatSystem.notifyAll(s"Team ${winningScore._1} won the event!")
  }
}
