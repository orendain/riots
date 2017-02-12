package controllers

import javax.inject.Inject

import model.{Secure, LoginForm}
import play.api.data.Forms._
import play.api.data._
import play.api.i18n.{MessagesApi, I18nSupport}
import play.api.mvc._
import play.filters.csrf.CSRF
import service.GuestIDSupervisor

class Login @Inject() (val messagesApi: MessagesApi) extends Controller with Secure with I18nSupport {

  val loginForm = Form (
    mapping(
      "username" -> nonEmptyText(minLength = 3, maxLength = 20),
      "password" -> nonEmptyText(minLength = 3)
    )(LoginForm.apply)(LoginForm.unapply)
  )

  def viewLogin = Action { implicit request =>
    implicit val token = CSRF.getToken(request)
    Ok(views.html.login(loginForm))
  }

  def checkLogin = Action { implicit request =>
    /*val filledForm = loginForm.bindFromRequest
    filledForm.fold (
      formWithErrors =>
        BadRequest(views.html.login(formWithErrors)(CSRF.getToken(request).get)),
      logForm => {
        UserRegistrar.loginPlayer(logForm.username, logForm.password) match {
          case Some(id) => Ok("Login check passed")
          case _ => BadRequest(views.html.login(filledForm.withError("password", "Wrong password"))(CSRF.getToken(request).get))
        }
      }
    )*/
    Ok("Pass1")
  }

  def submitLogin = Action { implicit request =>
    /*loginForm.bindFromRequest.fold (
      formWithErrors =>
        BadRequest(views.html.login(formWithErrors)(CSRF.getToken(request).get)),
      logForm => {
        UserRegistrar.loginPlayer(logForm.username, logForm.password) match {
          case Some(id) => Redirect(routes.Application.index).withSession("uid" -> id.toString)
          case _ => BadRequest(views.html.login(loginForm)(CSRF.getToken(request).get))
        }
      }
    )*/
    //Ok("Pass2")
    Results.Redirect(routes.Application.index()).
      withSession(request.session + (UID_Parameter -> "1"))
  }
}
