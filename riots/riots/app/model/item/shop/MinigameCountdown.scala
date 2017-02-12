package model.item.shop

import model.EntityData
import model.item.{ItemCreator, PowerPurchasableItem}
import shared.EntityStats

object MinigameCountdown extends ItemCreator {

  def apply(ownerData: EntityData) =
    apply(ownerData, Map.empty[String, String])

  def apply(ownerData: EntityData, keys: Map[String, String]) =
    new MinigameCountdown(ownerData, keys)
}

class MinigameCountdown(ownerData: EntityData, keys: Map[String, String]) extends ShopItem(ownerData, keys) with PowerPurchasableItem {

  val id = 2002
  val name = "Pocket Watch"
  val description = "Steal a pocket watch. View countdown until next minigame."
  val rawCost = 60L

  def applyTo(stats: EntityStats) = {
    ???
  }
}
