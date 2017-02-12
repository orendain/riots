package service

import model.item.ItemCreator
import model.item.cooldown._
import model.item.shop._
import model.item.upgrade._

object ItemDirectory {

  private val items = Seq[ItemCreator](
    // Cooldown Items
    StrengthIncrease1,

    // Shop Items
    SpeedMultiplier1,
    MinigameCountdown,

    // Upgrade Items
    SpeedUpgrade1
  )

  def item(id: Int) = items(id)

  val playerCooldownItem = Seq[ItemCreator](
    StrengthIncrease1
  )
  val playerShopItem = Seq[ItemCreator](
    MinigameCountdown,
    SpeedMultiplier1
  )
  val teamShopItem = Seq[ItemCreator](
    MinigameCountdown
  )
  val playerUpgradeItem = Seq[ItemCreator](
    SpeedUpgrade1
  )
  val teamUpgradeItem = Seq[ItemCreator](
    SpeedUpgrade1
  )
}
