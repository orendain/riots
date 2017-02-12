package service

import model.{TeamData, PlayerData}
import shared.{TeamID, PlayerID, Job}

/**
  * Updates player stats with appropriate gains/losses (timePlayed, gained resources, XP).
  * Updates team stats in nearly the same way as player stats.
  *
  * (Maybe) Update control point control.  Makes sense, after updating team stats.  However,
  * perhaps a separate control-point ticker/actor would be ideal -- especially if more features
  * come up for it that change how control changes.
  */
object Ticker {

  def tickPlayer(id: PlayerID) = {
    val data = EntityDatabase(id)

    // TODO: Would be more efficient to use dropWhile(),
    // but that would require reassignment.  var > val
    var recalc = false
    data.recalculations.foreach { time =>
      if (System.currentTimeMillis > time) {
        data.recalculations -= time
        recalc = true
      }
    }

    if (recalc) {
      ItemManager.recalculateStats(data)
    }

    data match {
      case d: PlayerData =>
        val stats = d.stats
        val bonus = d.job match {
          case Job.Gather => stats.gatherBonus
          case _ => 0
        }

        d.resources += stats.speed + bonus
        // TODO: replace magic number with time from config (store as global var for reuse across ticks)
        d.playTime += 2
    }

    UpdateManager.markChanged(id)
    UpdateManager.update(id)
  }

  def tickTeam(id: TeamID) = {
    val data = EntityDatabase(id)
    data match {
      case d: TeamData =>
        val stats = d.stats
        val bonus = d.stats.gatherBonus
        d.resources += stats.speed + bonus
    }

    UpdateManager.markChanged(id)
  }

  /*
  var teamSub = 0L
        Team.teams.keys foreach { k =>
          EntityDatabase(TeamID(k)) match { case ts: TeamStatBlock =>
            ts.stats.targetID match {
              case id => teamSub -= (ts.stats.speed + ts.stats.sabotageBonus)
            }
          }
        }
        s.stats.resources += s.stats.speed + s.stats.gatherBonus - teamSub
   */

}
