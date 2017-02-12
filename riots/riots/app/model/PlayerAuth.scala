package model

import scala.concurrent.ExecutionContext.Implicits.global

case class PlayerAuth(password: String)

object Authenticator {
/*
  // TODO: left/right or either might be more appropriate than Option
  def authenticate(username: String, password: String) = {
    val fut = Database2.read(username)
    fut map { player =>
      if (player.authData.password == password)
        Some(player.uid)
      else
        None
    }
  }
  */
}
