package model

import model.item.Item
import service.UpdateManager
import shared._

import scala.collection.mutable.Buffer
import scala.collection.mutable.ListBuffer

sealed trait EntityData {
  val id: ID
  val name: String
  var teamID: TeamID // TODO: make player only if possible

  val effects: Buffer[Item]
  val enabled: Buffer[Item]
  val recalculations: Buffer[Long]

  val stats: EntityStats

  var resources: Long
  var power: Long

  val updateEnabled = Buffer[Item]()
  def enableItem(item: Item*) = {
    updateEnabled ++= item

    //TODO: Might not be the best place to call UpdateManager?  Or maybe it's fine.
    UpdateManager.updateItems(id, updateEnabled)
  }
}


case class PlayerData(
  id: PlayerID,
  name: String,
  var teamID: TeamID,

  effects: ListBuffer[Item],
  enabled: ListBuffer[Item],
  recalculations: ListBuffer[Long],

  override val stats: PlayerStats,

  var resources: Long,
  var power: Long,

  //var teamID: TeamID,
  var job: Job,
  var playTime: Long)
extends EntityData


case class TeamData(
  id: TeamID,
  name: String,
  var teamID: TeamID,

  effects: ListBuffer[Item],
  enabled: ListBuffer[Item],
  recalculations: ListBuffer[Long],

  override val stats: TeamStats,

  var resources: Long,
  var power: Long,

  var targetID: TeamID)
extends EntityData
