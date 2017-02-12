package model.command

import play.api.libs.json._

import model.command.chat._
import model.command.misc._

object CommandImplicit {

  // Generic
  implicit val commandWrite: Writes[Command] = new Writes[Command] {
    def writes(cmd: Command) = { Json.obj() }
  }


  // Chat
  implicit val PlayerChatRead =
    (__ \ "msg").read[String].map(PlayerChatCmd(_))
  implicit val teamChatRead =
    (__ \ "msg").read[String].map(TeamChatCmd(_))

  implicit val outPlayerChatWrite = new Writes[OutPlayerChatCmd] {
    def writes(cmd: OutPlayerChatCmd) = {
        Json.obj(
        "cmd" -> "pch",
        "data" -> Json.obj(
          "msg" -> cmd.msg,
          "snd" -> cmd.snd,
          "cls" -> cmd.cls
        )
      )
    }
  }
  implicit val outTeamChatWrite = new Writes[OutTeamChatCmd] {
    def writes(cmd: OutTeamChatCmd) = Json.obj(
      "cmd" -> "tch",
      "data" -> Json.obj(
          "msg" -> cmd.msg,
          "snd" -> cmd.snd,
          "cls" -> cmd.cls
        )
    )
  }
  implicit val outNotificationChatWrite = new Writes[OutNotificationChatCmd] {
    def writes(cmd: OutNotificationChatCmd) = Json.obj(
      "cmd" -> "nch",
      "data" -> Json.obj(
          "msg" -> cmd.msg,
          "cls" -> cmd.cls
        )
    )
  }


  // Outgoing
  implicit val outPlayerUpdateWrite = new Writes[OutPlayerUpdateCmd] {
    def writes(cmd: OutPlayerUpdateCmd) = Json.obj(
      "cmd" -> "pup",
      "data" -> Json.obj(
        "str" -> cmd.str,
        "spd" -> cmd.spd,
        "ptc" -> cmd.ptc,
        "lck" -> cmd.lck,
        "dsc" -> cmd.dsc,
        "gtr" -> cmd.gtr,
        "sbt" -> cmd.sbt,
        "psr" -> cmd.psr,
        "pss" -> cmd.pss,
        "rsc" -> cmd.rsc
      )
    )
  }
  implicit val outTeamUpdateWrite = new Writes[OutTeamUpdateCmd] {
    def writes(cmd: OutTeamUpdateCmd) = Json.obj(
      "cmd" -> "tup",
      "data" -> Json.obj(
        "id" -> cmd.id,
        "str" -> cmd.str,
        "spd" -> cmd.spd,
        "ptc" -> cmd.ptc,
        "lck" -> cmd.lck,
        "dsc" -> cmd.dsc,
        "gtr" -> cmd.gtr,
        "sbt" -> cmd.sbt,
        "apl" -> cmd.apl
      )
    )
  }
  implicit val outAllUpdateWrite = new Writes[OutAllUpdateCmd] {
    def writes(cmd: OutAllUpdateCmd) = Json.obj(
      "cmd" -> "aup",
      "data" -> {
        val sb = new StringBuilder()
        Json.arr(cmd.ups map { _.toString } addString(sb, ",") toString)
      }
    )
  }

  implicit val outNewItemWrite = new Writes[OutNewItem] {
    def writes(cmd: OutNewItem) = Json.obj(
      "cmd" -> "nit",
      "data" -> Json.arr(cmd.its map { i =>
        Json.obj("nam" -> i.name, "des" -> i.desc, "lds" -> i.longDesc)
      })
    )
  }

  // Item Use
  implicit val outItemUseSuccessWrite = new Writes[OutItemUseSuccess] {
    def writes(cmd: OutItemUseSuccess) = Json.obj(
      "cmd" -> "ius",
      "data" -> Json.obj(
        "id" -> cmd.itemID
      )
    )
  }
  implicit val outItemUseFailWrite = new Writes[OutItemUseFail] {
    def writes(cmd: OutItemUseFail) = Json.obj(
      "cmd" -> "iuf",
      "data" -> Json.obj(
        "id" -> cmd.itemID
      )
    )
  }

  // Other
  implicit val useItemRead: Reads[UseItemCmd] =
    (__ \ "id").read[Int].map(UseItemCmd(_))
}
