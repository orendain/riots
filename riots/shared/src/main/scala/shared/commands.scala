package shared

sealed trait Command

// Chat ClientToServer
case class PlayerChatInCmd(msg: String) extends Command
case class TeamChatInCmd(msg: String) extends Command
// Chat ServerToClient
case class PlayerChatOutCmd(msg: String, snd: String, cls: String) extends Command
case class TeamChatOutCmd(msg: String, snd: String, cls: String) extends Command
case class NotificationChatOutCmd(msg: String, cls: String) extends Command


// Item ClientToServer
case class IndividualItemInCmd(itemID: Int) extends Command
case class TeamItemInCmd(itemID: Int) extends Command
// Item ServerToClient
case class ItemUseSuccessOutCmd(itemID: Int) extends Command
case class ItemUseFailOutCmd(itemID: Int) extends Command


sealed trait ItemUpdateType {
  val itemUpdate: ItemUpdate
}
case class IndividualItemUpdateOutCmd(itemUpdate: ItemUpdate) extends ItemUpdateType
case class TeamItemUpdateOutCmd(itemUpdate: ItemUpdate) extends ItemUpdateType

case class ItemUpdateOutCmd(itemUpdateType: ItemUpdateType) extends Command

// Updates ServerToClient
case class PlayerStatsUpdateOutCmd(stats: PlayerStatsUpdate) extends Command
case class TeamStatsUpdateOutCmd(teamID: TeamID, stats: TeamStatsUpdate) extends Command


// Misc
case class MultipleCmds(cmds: Iterable[Command]) extends Command
case class UnrecognizedCmd(msg: String) extends Command


// Event ServerToClient
sealed trait EventType {
  val message: String
}

case class EventClick1OptionOutCmd(message: String, click1: String) extends EventType
case class EventClick2OutCmd(message: String, click1: String, click2: String) extends EventType

case class EventStartOutCmd(eventID: Int, event: EventType) extends Command
// Event ClientToServer
case class EventActionInCmd(eventID: Int, action: EventActionCommand) extends Command
sealed trait EventActionCommand extends Command
case class Click1(option: Long) extends EventActionCommand
case class Click2(option: Long) extends EventActionCommand
