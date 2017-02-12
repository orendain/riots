package model.item.cooldown

import model.EntityData
import model.item.ItemCreator
import shared.EntityStats

object LuckIncrease1 extends ItemCreator {

  def apply(ownerData: EntityData) =
    apply(ownerData, Map.empty[String, String])

  def apply(ownerData: EntityData, keys: Map[String, String]) =
    new LuckIncrease1(ownerData, keys)
}

class LuckIncrease1(ownerData: EntityData, keys: Map[String, String]) extends CooldownItem(ownerData, keys) {

  val id = 1004
  val name = "Squeeze Zombie Rabbit Foot"
  val description = "Oooh, you're lucky! Temporarily increases luck."
  val rawCooldown = 120000L

  def magnitude = 5
  def activeApplyTo(stats: EntityStats) =
    stats.luck += magnitude
}
