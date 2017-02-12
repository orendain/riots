package shared

sealed trait ItemFace {
  val id: Int
  val name: String
  val desc: String
  val longDesc: String
}

case class CoolDownItemFace(
  override val id: Int,
  override val name: String,
  override val desc: String,
  override val longDesc: String,
  cooldown: Float) extends ItemFace

case class ShopItemFace(
  override val id: Int,
  override val name: String,
  override val desc: String,
  override val longDesc: String,
  cost: Long) extends ItemFace

case class UpgradeItemFace(
  override val id: Int,
  override val name: String,
  override val desc: String,
  override val longDesc: String,
  cost: Long,
  power: Long) extends ItemFace
