package shared

sealed trait EntityStats {

  var strength: Long
  var speed: Long
  var patience: Long
  var luck: Long

  var gatherBonus: Long
  var sabotageBonus: Long

  var resourceDiscount: Double
  var powerDiscount: Double

  def +=(sb: EntityStats) = {
    strength += sb.strength
    speed += sb.speed
    patience += sb.patience
    luck += sb.luck
  }

  def clear(): Unit
}


object PlayerStats {
  def empty = PlayerStats(1, 1, 0, 0, 0, 0, 0, 0)
}

case class PlayerStats(

  var strength: Long,
  var speed: Long,
  var patience: Long,
  var luck: Long,

  var gatherBonus: Long,
  var sabotageBonus: Long,

  var resourceDiscount: Double,
  var powerDiscount: Double)
extends EntityStats {
  def clear() {
    strength = 1
    speed = 1
    patience = 0
    luck = 0

    gatherBonus = 0
    sabotageBonus = 0

    resourceDiscount = 0
    powerDiscount = 0
  }
}


object TeamStats {
  def empty = TeamStats(0, 0, 0, 0, 0, 0, 0, 0, 0)
}

case class TeamStats(

  var strength: Long,
  var speed: Long,
  var patience: Long,
  var luck: Long,

  var gatherBonus: Long,
  var sabotageBonus: Long,

  var resourceDiscount: Double,
  var powerDiscount: Double,

  var activePlayers: Int)
extends EntityStats {
  def clear() {
    strength = 0
    speed = 0
    patience = 0
    luck = 0

    gatherBonus = 0
    sabotageBonus = 0

    resourceDiscount = 0
    powerDiscount = 0

    activePlayers = 0
  }
}
