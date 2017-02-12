package service

import play.api.libs.json.{JsValue, Json}
import shared._
import upickle.default._

// TODO: Small enough to include source where needed?
// Or make into inheritable trait (nah, its not "a formatter" ... but it could be an actor "with" a formatter.)?

object CommandFormatter {

  type CommandType = JsValue

  def parse(json: JsValue) = try {
    read[Command](json.toString)
  } catch {
    case e: Throwable => UnrecognizedCmd(s"$e")
  }

  def build(cmd: Command) = {
    val v = Json.obj("cmd" -> write(cmd))
    //Logger.debug("CommandFormatter: Wrote " + v)
    v
  }
}
