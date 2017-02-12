package model.item.upgrade

import model.EntityData
import model.item.{ItemCreator, ResourcePurchasableItem}
import shared.EntityStats

object SpeedUpgrade1 extends ItemCreator {

  def apply(ownerData: EntityData) =
    apply(ownerData, Map.empty[String, String])

  def apply(ownerData: EntityData, keys: Map[String, String]) =
    new SpeedUpgrade1(ownerData, keys)
}

class SpeedUpgrade1(ownerData: EntityData, keys: Map[String, String]) extends UpgradeItem(ownerData, keys) with ResourcePurchasableItem {

  val id = 3001
  val name = "Legs"
  val description = "Chew decay off from your legs. Increases speed."
  val rawCost = 50L

  def magnitude = 5
  def applyTo(stats: EntityStats) = {
    stats.speed += magnitude
    stats
  }
}
