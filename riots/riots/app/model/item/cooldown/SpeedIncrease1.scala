package model.item.cooldown

import model.EntityData
import model.item.ItemCreator
import shared.EntityStats

object SpeedIncrease1 extends ItemCreator {

  def apply(ownerData: EntityData) =
    apply(ownerData, Map.empty[String, String])

  def apply(ownerData: EntityData, keys: Map[String, String]) =
    new SpeedIncrease1(ownerData, keys)
}

class SpeedIncrease1(ownerData: EntityData, keys: Map[String, String]) extends CooldownItem(ownerData, keys) {

  val id = 1002
  val name = "Sprint"
  val description = "Run faster! Temporarily increases speed."
  val rawCooldown = 25000L

  def magnitude = 5
  def activeApplyTo(stats: EntityStats) =
    stats.speed += magnitude
}
