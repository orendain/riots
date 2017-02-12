package model.item.cooldown

import model.EntityData
import model.item.ItemCreator
import shared.EntityStats

object StrengthIncrease1 extends ItemCreator {

  def apply(ownerData: EntityData) =
    apply(ownerData, Map.empty[String, String])

  def apply(ownerData: EntityData, keys: Map[String, String]) =
    new StrengthIncrease1(ownerData, keys)
}

class StrengthIncrease1(ownerData: EntityData, keys: Map[String, String]) extends CooldownItem(ownerData, keys) {

  val id = 1001
  val name = "Eat a human"
  val description = "Consume human flesh! Temporarily increases strength."
  val rawCooldown = 20000L

  def magnitude = 5
  def activeApplyTo(stats: EntityStats) =
    stats.strength += magnitude
}
