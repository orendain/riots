package model.item.shop

import model.EntityData
import model.item.{ItemCreator, ResourcePurchasableItem}
import shared.EntityStats

object SpeedMultiplier1 extends ItemCreator {

  def apply(ownerData: EntityData) =
    apply(ownerData, Map.empty[String, String])

  def apply(ownerData: EntityData, keys: Map[String, String]) =
    new SpeedMultiplier1(ownerData, keys)
}

class SpeedMultiplier1(ownerData: EntityData, keys: Map[String, String]) extends ShopItem(ownerData, keys) with ResourcePurchasableItem {

  val id = 2001
  val name = "Shoes"
  val description = "Wear almost-disintegrated shoes. Temporarily doubles speed."
  val rawCost = 60L

  def applyTo(stats: EntityStats) = {
    stats.speed *= 2
    stats
  }
}
