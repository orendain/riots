package service

import model.event.Event

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object EventManager {
  var eventsInProgress = Array[Event]()
  var eventsInQueue = Array[Event]()

/*
  def isTeamBusy(team: Team) = {
    !(for {
      e <- eventsInProgress;
      t <- e.teams;
      if t == team
    } yield t).isEmpty
  }*/

  def queueEvent(event: Event) = {
    eventsInQueue :+ event
    process()
  }

  private def process() = Future {
    /*
    eventsInQueue foreach { ev =>
      (ev.teams count ((t: Team) => isTeamBusy(t) == true) == 0) &&
      (eventsInProgress += ev) &&
      (eventsInQueue -= ev) &&
      start(ev)
    }
    */

    /*for {
      ev <- eventsInQueue
      if ((ev.teams count (t => isTeamBusy(t) == true)) == 0)
    } {
      eventsInProgress :+ ev
      eventsInQueue -= ev
      start(ev)
    }*/
  }

  private def start(event: Event) {
    //val teamplayers = UserStore.teamplayers
    // event should have the event text in it, as well
    // as the text for the other team (defender vs attacker)

    // allow each team to run itself?
  }
}
