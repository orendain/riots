package service

import model.item.Item
import model.item.cooldown.CooldownItem
import model.item.shop.ShopItem
import model.item.upgrade.UpgradeItem
import model.{PlayerData, TeamData}
import play.api.Logger
import shared._

import scala.collection.mutable
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object UpdateManager {

  val log = Logger(this.getClass)

  val playerUpdates = mutable.HashSet[PlayerID]()
  val teamUpdates = mutable.Map[TeamID, Command]()

  def markChanged(id: ID) {
    id match {
      case i: PlayerID => playerUpdates += i
      case i: TeamID => teamUpdates += (i -> buildUpdate(i))
    }
  }

  def hasUpdate(id: ID) = {
    id match {
      case i: PlayerID => playerUpdates(i)
    }
  }

  def update(id: ID) = Future {
    id match {
      case i: PlayerID =>
        hasUpdate(id) match {
          case true =>
            playerUpdates -= i
            CommandRelayer.send(id, MultipleCmds(teamUpdates.values ++ Seq(buildUpdate(i))))
          case false =>
            CommandRelayer.send(id, MultipleCmds(teamUpdates.values))
        }
      case _ =>
    }
  }

  def updateGuests() = Future {
    CommandRelayer.sendToGuests(MultipleCmds(teamUpdates.values))
  }

  def update(id: ID, cmd: Command): Unit = Future {
    id match {
      case i: PlayerID =>
        CommandRelayer.send(id, cmd)
      case i: TeamID =>
        ConnectionStore.players.filter{ case (pid, con) => EntityDatabase(pid).teamID == i }.keys.foreach{ pid =>
          update(pid, cmd)
        }
    }
  }

  private def buildUpdate(id: ID) = {
    EntityDatabase(id) match {
      case d: PlayerData =>
        val cmd = PlayerStatsUpdate(d.resources, d.power, d.job, d.playTime,
          d.stats.strength, d.stats.speed, d.stats.patience, d.stats.luck,
          d.stats.resourceDiscount)
        PlayerStatsUpdateOutCmd(cmd)
      case d: TeamData =>
        val cmd = TeamStatsUpdate(d.resources, d.power, d.targetID,
          d.stats.strength, d.stats.speed, d.stats.resourceDiscount, d.stats.activePlayers)
        TeamStatsUpdateOutCmd(id, cmd)
    }
  }

  def updateItems(id: ID, items: Seq[Item]) = {
    val data = EntityDatabase(id)
    val updates = items map { item =>
      item match {
        case i: CooldownItem =>
          CooldownItemUpdate(i.id, i.name, i.description, i.cooldown)
        case i: ShopItem =>
          ShopItemUpdate(i.id, i.name, i.description, i.cost)
        case i: UpgradeItem =>
          UpgradeItemUpdate(i.id, i.name, i.description, i.cost)
      }
    }

    val updateCmds = data match {
      case x: PlayerData => updates map { x => ItemUpdateOutCmd(IndividualItemUpdateOutCmd(x)) }
      case x: TeamData => updates map { x => ItemUpdateOutCmd(TeamItemUpdateOutCmd(x)) }
    }
    update(id, MultipleCmds(updateCmds))
  }
}
