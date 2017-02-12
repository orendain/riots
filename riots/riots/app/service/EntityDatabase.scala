package service

import model._
import model.item.Item
import model.item.cooldown._
import model.item.shop._
import model.item.upgrade._
import play.api.Logger
import shared._

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

object EntityDatabase {

  val log = Logger(this.getClass)

  val data = mutable.Map[ID, EntityData]()

  // TODO: Remove, for testing only
  load()

  // TODO: should return a copy and not original?
  def apply(id:ID) =
    data(id)

  def add(newEntity: EntityData) =
    data += (newEntity.id -> newEntity)

  def newPlayer(pid: PlayerID, name: String, tid: TeamID) = {

    val startingPlayerEffects = ListBuffer[Item]()
    val startingPlayerItems = ListBuffer[Item]()
    val startingPlayerRecalculations = ListBuffer[Long]()
    val startingPlayerJob = Job.Gather

    val data = PlayerData(
      pid,
      name,
      tid,
      startingPlayerEffects,
      startingPlayerItems,
      startingPlayerRecalculations,
      PlayerStats.empty,
      0,
      0,
      startingPlayerJob,
      0)

    val startingPlayerInitializedItems = Seq[Item](
      // Cooldowns
      SpeedIncrease1(data),
      StrengthIncrease1(data),
      // Upgrades
      SpeedUpgrade1(data),
      StrengthUpgrade1(data),
      CooldownUpgrade(data),
      // Shop
      SpeedMultiplier1(data)
    )

    data.enabled ++= startingPlayerInitializedItems
    data
  }

  def newTeam(tid: TeamID, name: String) = {

    val startingTeamEffects = ListBuffer[Item]()
    val startingTeamItems = ListBuffer[Item]()
    val startingTeamRecalculations = ListBuffer[Long]()

    val data = TeamData(
      tid,
      name,
      tid,
      startingTeamEffects,
      startingTeamItems,
      startingTeamRecalculations,
      TeamStats.empty,
      0,
      0,
      tid)

    val startingTeamInitializedItems = Seq[Item](
      // Cooldowns
      // (none)
      // Upgrades
      StrengthUpgrade1(data),
      // Shop
      SpeedMultiplier1(data)
    )

    data.enabled ++= startingTeamInitializedItems
    data
  }

  def load() {
    add(newTeam(TeamID(1), "Radioactive Regime"))
    add(newTeam(TeamID(2), "Contamination Crew"))
    add(newTeam(TeamID(3), "Spitter Squad"))
    add(newTeam(TeamID(4), "Undead Union"))

    add(newPlayer(PlayerID(1), "edgar", TeamID(1)))
  }
}
