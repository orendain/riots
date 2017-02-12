package controllers

import javax.inject.Inject

import model.RegisterForm
import play.api.Logger
import play.api.data.Forms._
import play.api.data._
import play.api.data.validation._
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._
import play.filters.csrf.CSRF


class Register @Inject() (val messagesApi: MessagesApi) extends Controller with I18nSupport {

  val usernameCheck: Constraint[String] = Constraint("constraints.validUsername") { text =>
    if (text.trim contains " ") {
      // TODO: check for invalid (non-128-ascii) characters
      Invalid(ValidationError("Username has invalid characters"))
    //} else if (UserRegistrar.isNameTaken(text.trim)) {
    //  Invalid(ValidationError("Username is taken"))
    } else {
      Valid
    }
  }

  val passwordCheck: Constraint[(String, String)] = Constraint("constraints.validPassword", "DFSDF") {
    passwords => (passwords._1 == passwords._2) match {
      case true => Valid
      case _ => Invalid(Seq(ValidationError("Passwords do not match")))
    }
  }

  val validUsername: Mapping[String] =
    nonEmptyText(minLength = 3, maxLength = 20).verifying(usernameCheck)

  val registerForm = Form(
    mapping(
      "username" -> validUsername,
      "passwords" -> tuple(
        "password" -> nonEmptyText(minLength = 3),
        "confirmPassword" -> nonEmptyText
      ).verifying(passwordCheck),
      "teamID" -> nonEmptyText
    )(RegisterForm.apply)(RegisterForm.unapply)
  )

  val teams = RegisterForm.teams

  def viewRegister = Action { implicit request =>
    implicit val token = CSRF.getToken(request)
    Ok(views.html.register(registerForm, teams))
  }

  def checkRegister = Action { implicit request =>
    registerForm.bindFromRequest.fold (
      formWithErrors => {
        implicit val token = CSRF.getToken(request)
        BadRequest(views.html.register(formWithErrors, teams))
      },
      regForm => {
        Logger.debug("Login: Check Registration 2")
        Ok("Registration check passed")
      }
    )
  }

  def submitRegister = Action { implicit request =>
    registerForm.bindFromRequest.fold (
      formWithErrors => {
        implicit val token = CSRF.getToken(request)
        BadRequest(views.html.register(formWithErrors, teams))
      },
      regForm => {
        Logger.debug("Login: Submit Registration 2")
        /*UserRegistrar.registerPlayer(regForm.username, regForm.passwords._1, regForm.teamID.toInt) match {
          case Some(id) => Redirect(routes.Application.index).withSession("uid" -> id.toString)
          case _ => BadRequest(views.html.register(registerForm, teams)(CSRF.getToken(request).get))
        }*/
        Ok("Okay")
      }
    )
  }
}
