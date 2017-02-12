package model.item.cooldown

import model.EntityData
import model.item.Item
import shared.EntityStats

abstract class CooldownItem(val ownerData: EntityData, val keys: Map[String, String]) extends Item {

  val rawCooldown: Long
  var expiration = keys.getOrElse("expiration", "0").toLong

  def activeApplyTo(stats: EntityStats)

  def cooldown = {
    //TODO: use ownerData.cooldownMultiplier
    (rawCooldown * 1.0).toLong
  }

  override def use() =
    if (System.currentTimeMillis > expiration) {
      expiration = System.currentTimeMillis + cooldown
      // TODO: not all cooldowns would need recalcs.  some might just perm +5 resources
      ownerData.recalculations += expiration
      super.use()
    } else {
      false
    }

  def applyTo(stats: EntityStats) = {
    if (System.currentTimeMillis < expiration) {
      activeApplyTo(stats)
    } else {
      ownerData.effects -= this
    }
    stats
  }
}
