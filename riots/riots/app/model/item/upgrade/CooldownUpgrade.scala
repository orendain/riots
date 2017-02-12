package model.item.upgrade

import model.EntityData
import model.item.cooldown.{LuckIncrease1, StrengthIncrease1, PatienceIncrease1}
import model.item.{ItemCreator, ResourcePurchasableItem}
import shared.EntityStats

object CooldownUpgrade extends ItemCreator {

  def apply(ownerData: EntityData) =
    apply(ownerData, Map.empty[String, String])

  def apply(ownerData: EntityData, keys: Map[String, String]) =
    new CooldownUpgrade(ownerData, keys)
}

class CooldownUpgrade(ownerData: EntityData, keys: Map[String, String]) extends UpgradeItem(ownerData, keys) with ResourcePurchasableItem {

  val id = 3003
  val name = "Cooldowns"
  val description = "Stuff. Unlocks more cooldowns."
  val rawCost = 50L

  val items = Seq(
    1 -> Seq(LuckIncrease1, StrengthIncrease1),
    2 -> Seq(PatienceIncrease1)
  )

  override def use() = {
    if (buy()) {
      val newItems = items(1) match {
        case (lvl, itmcrts) => itmcrts map { itmcrt => itmcrt(ownerData) }
      }
      ownerData.enableItem(newItems: _*)
      true
    } else {
      false
    }
  }

  def magnitude = 5
  def applyTo(stats: EntityStats) = {
    // Nothing
    stats
  }
}
