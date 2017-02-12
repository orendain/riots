package controllers

import javax.inject.Inject

import model.ContactForm
import play.api.data.Forms._
import play.api.data._
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._
import play.filters.csrf.CSRF

class Contact @Inject() (val messagesApi: MessagesApi) extends Controller with I18nSupport {

  val contactForm = Form(
    mapping(
      "email" -> email,
      "subject" -> nonEmptyText,
      "message" -> nonEmptyText
    )(ContactForm.apply)(ContactForm.unapply)
  )

  def viewContact = Action { implicit request =>
    implicit val token = CSRF.getToken(request)
    Ok(views.html.contact(contactForm))
  }

  def submitContact = Action { implicit request =>
    contactForm.bindFromRequest.fold (
      formWithErrors => {
        implicit val token = CSRF.getToken(request)
        BadRequest(views.html.contact(formWithErrors))
      },
      conForm => {
        Ok(views.html.components.contactSent())
      }
    )
  }
}
