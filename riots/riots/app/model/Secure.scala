package model

import controllers.routes
import play.api.mvc._
import service.GuestIDSupervisor

/**
  *
  */
trait Secure {

  val UID_Parameter = "uid"

  /**
    * Retrieve the connected user's userid, if available.
    */
  def userId(request: RequestHeader) = request.session.get(UID_Parameter)

  /**
    * Return a 401 Unauthorized if the connected user lacks access.
    */
  private def onUnauthorized(request: RequestHeader) =
    Results.Redirect(routes.Application.index()).
      withSession(request.session + (UID_Parameter -> GuestIDSupervisor.nextGuestID().toString))

  /**
    * Function to check if a user is authenticated.
    */
  def isAuthenticated(result: Result) =
    Security.Authenticated(userId, onUnauthorized) { user =>
      Action(result)
    }
}
