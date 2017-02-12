package model

import shared._

case class RegisterForm (
  username: String,
  passwords: (String, String),
  teamID: String
)

object RegisterForm {
  val teams = (for { t <- Team.teams } yield (t._1.toString -> t._2.name)) toSeq
}
