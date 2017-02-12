package controllers

import javax.inject.Inject

import model.{Secure, UserConnection}
import play.api.Logger
import play.api.Play.current
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.json.JsValue
import play.api.mvc._
import service.{ ConnectionManager}

class Application @Inject() (val messagesApi: MessagesApi) extends Controller with Secure with I18nSupport {

  private val log = Logger(this.getClass)

  /**
    *
    */
  def index = isAuthenticated {
    //Database2.insert()
//    Database2.read("nameF")
//    Database2.listAll()
    //Database2.deleteAll()

    // TODO: Temporary.  This belongs *probably* in a dependency-injected class
    // ... as maybe do most objects in the services package.
    service.ActorCoordinator.guestsActr

    Ok(views.html.index())
  }

  def websocket = WebSocket.acceptWithActor[JsValue, JsValue] { request => outActr =>
    val uid = userId(request).getOrElse("0").toInt
    log.debug(s"UID $uid attempting to connect to websocket.")
    val con = UserConnection(uid, outActr)
    ConnectionManager.connect(con)
  }
}
