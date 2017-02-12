package model.item.cooldown

import model.EntityData
import model.item.ItemCreator
import shared.EntityStats

object PatienceIncrease1 extends ItemCreator {

  def apply(ownerData: EntityData) =
    apply(ownerData, Map.empty[String, String])

  def apply(ownerData: EntityData, keys: Map[String, String]) =
    new PatienceIncrease1(ownerData, keys)
}

class PatienceIncrease1(ownerData: EntityData, keys: Map[String, String]) extends CooldownItem(ownerData, keys) {

  val id = 1003
  val name = "Inhale Toxic Fumes"
  val description = "Get drugged up! Temporarily increases patience."
  val rawCooldown = 60000L

  def magnitude = 5
  def activeApplyTo(stats: EntityStats) =
    stats.patience += magnitude
}
