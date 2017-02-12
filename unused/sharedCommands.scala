/*


package shared.command

sealed trait Command

// Chat Incoming
case class PlayerChatCmd(msg: String) extends Command
case class TeamChatCmd(msg: String) extends Command

// Chat Outgoing
case class OutPlayerChatCmd(msg: String, snd: String, cls: String) extends Command
case class OutTeamChatCmd(msg: String, snd: String, cls: String) extends Command
case class OutNotificationChatCmd(msg: String, cls: String) extends Command

// Item Incoming
case class UseItemCmd(id: Int) extends Command
// Item Outgoing
case class OutItemUseSuccess(itemID: Int) extends Command
case class OutItemUseFail(itemID: Int) extends Command

// Updates
case class OutPlayerUpdateCmd(str: Long, spd: Long, ptc: Long, lck: Long,
  dsc: Double, gtr: Long, sbt: Long, psr: Long, pss: Long, rsc: Long) extends Command
case class OutTeamUpdateCmd(id: Int, str: Long, spd: Long, ptc: Long, lck: Long,
  dsc: Double, gtr: Long, sbt: Long, apl: Long) extends Command
case class OutAllUpdateCmd(ups: Iterable[Command]) extends Command
//case class OutNewItem(its: Iterable[Item]) extends Command

// General State
//case class ErrorCmd(msg: String) extends Command
//case class SuccessCmd(msg: String) extends Command
case class IgnoredCmd() extends Command






*/



// Event Commands
/*
sealed abstract class EventCommand extends Command

case object Click1 extends EventCommand
case object Click2 extends EventCommand
case object PowerPoint extends EventCommand

object EventCommand {

  def parse(json: JsValue) =
  (json \ "type").as[String] match {
    case "ck1" => Click1
    case "ck2" => Click2
    case "pps" => PowerPoint
  }
}
*/
