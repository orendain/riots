package model.item.upgrade

import model.EntityData
import model.item.{ItemCreator, ResourcePurchasableItem}
import shared.EntityStats

object StrengthUpgrade1 extends ItemCreator {

  def apply(ownerData: EntityData) =
    apply(ownerData, Map.empty[String, String])

  def apply(ownerData: EntityData, keys: Map[String, String]) =
    new StrengthUpgrade1(ownerData, keys)
}

class StrengthUpgrade1(ownerData: EntityData, keys: Map[String, String]) extends UpgradeItem(ownerData, keys) with ResourcePurchasableItem {

  val id = 3002
  val name = "Teeth"
  val description = "Sharpen your teeth. Increases strength."
  val rawCost = 50L

  def magnitude = 5
  def applyTo(stats: EntityStats) = {
    stats.strength += magnitude
    stats
  }
}
