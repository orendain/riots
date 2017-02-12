package model.item

import model.EntityData
import play.api.Logger
import shared.EntityStats

trait Descriptive {
  val name: String
  val description: String
}

trait ItemCreator {
  def apply(ownerData: EntityData): Item
  def apply(ownerData: EntityData, keys: Map[String, String]): Item
}

//abstract class Item(val ownerData: EntityData, val keys: Map[String, String]) extends Descriptive {
trait Item extends Descriptive {

  val id: Int
  val ownerData: EntityData
  val keys: Map[String, String]

  // Temporary
  val log = Logger(this.getClass)

  def use(): Boolean = {
    ownerData.effects += this
    log.debug(s"Effects: ${ownerData.effects}")
    true
  }

  def applyTo(stats: EntityStats): EntityStats
}

trait PurchasableItem extends Item {

  val rawCost: Long
  def cost = calcCost(rawCost)
  // Helper method to allow traits to chime in
  def calcCost(rawCost: Long) = rawCost * 1

  def buy(): Boolean

  override def use() = {
    if (buy()) {
      super.use()
    } else {
      false
    }
  }
}

trait ResourcePurchasableItem {

  this: PurchasableItem =>

  override def calcCost(rawCost: Long) = (rawCost * ownerData.stats.resourceDiscount).toLong

  def buy() =
    if (ownerData.resources >= calcCost(rawCost)) {
      ownerData.resources -= calcCost(rawCost)
      true
    } else {
      false
    }
}

trait PowerPurchasableItem {

  this: PurchasableItem =>

  override def calcCost(rawCost: Long) = (rawCost * ownerData.stats.powerDiscount).toLong

  def buy() =
    if (ownerData.power >= calcCost(rawCost)) {
      ownerData.power -= calcCost(rawCost)
      true
    } else {
      false
    }
}
