package service

import actor.TeamActr
import model._
import play.api.Logger
import shared._

object ItemManager {

  val log = Logger(this.getClass)

  def useIndividualItem(id: PlayerID, itemID: Int): Unit = {
    val entityData = EntityDatabase(id)
    val item = entityData.enabled.collectFirst { case i if i.id == itemID => i }
    item match {
      case Some(it) =>
        if (it.use()) {
          log.debug(s"Player ${id.id} successfully used item ID $itemID")
          recalculateStats(entityData)
          UpdateManager.markChanged(entityData.id)
          // TODO: could skip and step and update immediately -- dont need to mark update for change
          // unless for some reason there is a reason I'd want to hold back a change in the user's stats from updating
          UpdateManager.update(entityData.id)
        }
      case _ => log.debug(s"Player ${id.id} could not use item ID $itemID")
    }
  }

  def useTeamItem(id: PlayerID, itemID: Int) {
    val teamID = EntityDatabase(id).teamID
    val entityData = EntityDatabase(teamID)
    val item = entityData.enabled.collectFirst { case i if i.id == itemID => i }
    item match {
      case Some(it) =>
        // TODO: Go to a vote
        ActorCoordinator.teamActrs(teamID) ! TeamActr.ItemVote(id, it)
      case _ => log.debug(s"User ${id.id} from team ${teamID.id} could not use item ID $itemID")
    }
  }

  // TODO: recalc doesn't belong in an ITEM manager.
  def recalculateStats(entityData: EntityData) {
    entityData match {
      case data: PlayerData =>
        data.stats.clear()
        log.debug(s"Effects list: ${data.effects}.")
        data.effects foreach { it =>
          it.applyTo(data.stats)
          log.debug(s"Applied to an item.")
        }
        log.debug(s"New strength: ${data.stats.strength}")

        //TODO: Belongs here, sure, but use of actors sort of ugly-fying things.
        ActorCoordinator.teamActrs(data.teamID) ! TeamActr.PlayerStatsUpdated
      case data: TeamData =>
        data.stats.clear()
        ConnectionStore.players.keys foreach { id =>
          val stats = EntityDatabase(id).stats
          log.debug(s"Adding player $id stats to team stats: $stats")
          data.stats += stats
        }
    }
  }

  // TODO: Instead of just marking a team as changed, immediately update the players with those changes
  // Will make frontend seem more smooth.  2 updates WITHIN 2 seconds looks smoother than 2 updates EVERY 2 seconds.
  def recalculateTeamStats(id: TeamID) = {
    val entityData = EntityDatabase(id)
    recalculateStats(entityData)
    UpdateManager.markChanged(id)
  }
}
