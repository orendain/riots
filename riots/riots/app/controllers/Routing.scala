package controllers

import javax.inject.Inject

import play.api.i18n.{MessagesApi, I18nSupport}
import play.api.mvc._
import play.api.routing.JavaScriptReverseRouter


class Routing @Inject() (val messagesApi: MessagesApi) extends Controller with I18nSupport {

  /**
   *
   */
  def javascriptRoutes = Action { implicit request =>
    Ok(JavaScriptReverseRouter("jsRoutes")(
      routes.javascript.Register.checkRegister,
      routes.javascript.Login.checkLogin,
      routes.javascript.Contact.submitContact
    )).as("text/javascript")
  }
}

