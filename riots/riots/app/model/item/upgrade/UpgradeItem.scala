package model.item.upgrade

import model.EntityData
import model.item.PurchasableItem

abstract class UpgradeItem(val ownerData: EntityData, val keys: Map[String, String]) extends PurchasableItem {

  var level = keys.getOrElse("level", "0").toInt

  override def calcCost(rawCost: Long) = super.calcCost(math.pow(2, level).toLong)
}
