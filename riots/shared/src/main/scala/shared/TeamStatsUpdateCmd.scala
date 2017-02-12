package shared

case class PlayerStatsUpdate(

  resources: Long,
  power: Long,

  job: Job,
  playTime: Long,

  strength: Long,
  speed: Long,
  patience: Long,
  luck: Long,

  discount: Double)

case class TeamStatsUpdate(

  resources: Long,
  power: Long,
  targetID: TeamID,

  strength: Long,
  speed: Long,

  discount: Double,

  activePlayers: Int
)


// TODO: Might not have to be sealed, doing it to play safe with upickle for now
sealed trait ItemUpdate {
  val id: Int
  val name: String
  val description: String
}

case class CooldownItemUpdate(

  id: Int,
  name: String,
  description: String,
  cooldown: Long
) extends ItemUpdate

case class ShopItemUpdate(

  id: Int,
  name: String,
  description: String,
  cost: Double
) extends ItemUpdate

case class UpgradeItemUpdate(

  id: Int,
  name: String,
  description: String,
  cost: Double
) extends ItemUpdate
