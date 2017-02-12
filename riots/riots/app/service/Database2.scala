package service

import model.{PlayerData, PlayerAuth}
import play.api.Logger
import play.api.libs.json.Json
import play.modules.reactivemongo.json.collection.JSONCollection
import reactivemongo.bson.BSONObjectID
import shared.PlayerID
import scala.collection.mutable
import scala.concurrent.ExecutionContext.Implicits.global

import play.api.Play.current
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.core.actors.Exceptions.PrimaryUnavailableException

//case class TestPlayer(_id: BSONObjectID, authData: PlayerAuth)
//case class TestPlayer(_id: BSONObjectID, name: String, count: Int, someLong: Long)
case class TestPlayer(_id: BSONObjectID, uid: Int, username: String, authData: PlayerAuth, playerData: PlayerData)

//import play.modules.reactivemongo.json._

/*

object TestJsonFormats {
  import play.api.libs.json.Json
//  import play.api.data._
//  import play.api.data.Forms._
  import play.modules.reactivemongo.json.BSONFormats._

  // Generates Writes and Reads for Feed and User thanks to Json Macros
  implicit val testPlayerIDFormat = Json.format[PlayerID]
  implicit val testPlayerDataFormat = Json.format[PlayerData]
  implicit val testPlayerAuthFormat = Json.format[PlayerAuth]
  implicit val testPlayerFormat = Json.format[TestPlayer]

}


object Database2 {

  import TestJsonFormats._


  private val log = Logger(this.getClass)

  //val reactiveMongoApi: ReactiveMongoApi
  lazy val reactiveMongoApi = current.injector.instanceOf[ReactiveMongoApi]

  def collection: JSONCollection = reactiveMongoApi.db.collection[JSONCollection]("riot1")

  def listAll() = {
    val fut = collection.genericQueryBuilder.cursor[TestPlayer]().collect[List]()
    log.debug("About to list.")
    fut map { lst =>
      lst.foreach { tp =>
        log.debug(s"Here: $tp")
        log.debug(s"Auth: ${tp.authData}")
      }
    }
  }

  import play.modules.reactivemongo.json._
  //import scala.concurrent.ExecutionContext.Implicits.global.

  def read(username: String) = {
    val query = Json.obj("username" -> username)
    //val query = Json.obj("username" -> username)
    val fut = collection.find(query).cursor[TestPlayer]().collect[List](1)
    log.debug("About to read.")
    fut map { lst =>
      log.debug(s"Read fut: $lst")
      lst.foreach { tp =>
        log.debug(s"Read here: $tp")
      }
      lst.head
    }
  }

  def insert(player: TestPlayer) = {
    //val player = TestPlayer(BSONObjectID.generate, "nameH", 212, 123L)
    //val player = TestPlayer(BSONObjectID.generate, 1, "nameF", PlayerAuth("pH"))
    val futureUpdateEmp = collection.insert(player)
    log.debug(s"About to add")
    futureUpdateEmp.map { result =>
      log.debug(s"Result: $result")
    }.recover {
      case PrimaryUnavailableException =>
        log.debug("Please install MongoDB.")
        //InternalServerError("Please install MongoDB")
      case _ => log.debug("Nope.")
    }
  }

  def delete(id: String) = {
    val futureInt = collection.remove(Json.obj("_id" -> Json.obj("$oid" -> id)), firstMatchOnly = true)
    futureInt.map { result =>
      log.debug(s"Delete: $result")
    }.recover {
      case PrimaryUnavailableException =>
        log.error("Problem deleting employee")
      case _ => log.debug("Nope.")
    }
  }

  def deleteAll() = {
    val futureInt = collection.remove(Json.obj(), firstMatchOnly = false)
    futureInt.map { result =>
      log.debug("Deleting all")
    }.recover {
      case PrimaryUnavailableException =>
        log.error("Problem deleting employee")
      case _ => log.debug("Nope.")
    }
  }
}

*/