package shared

sealed trait ID { val id: Int }

case class PlayerID(override val id: Int) extends ID
case class GuestID(override val id: Int) extends ID
case class TeamID(override val id: Int) extends ID

object ID {
  implicit def ID2PlayerID(id: ID): PlayerID = id match { case i: PlayerID => i }
  implicit def ID2GuestID(id: ID): GuestID = id match { case i: GuestID => i }
  implicit def ID2TeamID(id: ID): TeamID = id match { case i: TeamID => i }
}
