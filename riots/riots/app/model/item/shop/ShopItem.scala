package model.item.shop

import model.EntityData
import model.item.PurchasableItem

abstract class ShopItem(val ownerData: EntityData, val keys: Map[String, String]) extends PurchasableItem
