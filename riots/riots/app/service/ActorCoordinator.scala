package service

import actor.{GuestsActr, TeamActr, TeamsActr}
import akka.actor.ActorSystem
import play.api.{Logger, Play}
import shared.{Team, TeamID}

// TODO: probably use an Akka-executioncontext?
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object ActorCoordinator {

  val log = Logger(this.getClass)

  val system = ActorSystem("riots")

  val teamsActr = system.actorOf(TeamsActr.props(), "TeamActr")
  val guestsActr = system.actorOf(GuestsActr.props(), "GuestsActr")
  val teamActrs = Team.teams.map { case (tid, name) =>
    val id = TeamID(tid)
    id -> system.actorOf(TeamActr.props(id), s"TeamActr-${tid}")
  }

  start()

  def start() {
    val teamTickFreq = Play.current.configuration.getInt("teams.tickFrequency").getOrElse(2)
    val guestTickFreq = Play.current.configuration.getInt("guest.tickFrequency").getOrElse(2)

    //system.scheduler.schedule(0 second, teamTickFreq second, teamsActr, TeamsActr.Tick)
    //system.scheduler.schedule(0 second, guestTickFreq second, guestsActr, GuestsActr.Tick)

    teamActrs.foreach { case (id, actrRef) =>
      system.scheduler.schedule(0 second, teamTickFreq second, actrRef, TeamActr.Tick)
    }
  }
}
