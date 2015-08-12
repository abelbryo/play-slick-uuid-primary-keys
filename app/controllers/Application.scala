package controllers

import play.api._
import play.api.mvc._
import scala.util.{Try, Success, Failure}
import java.util.UUID

class Application extends Controller {

  import models._

  def index = Action {
    val list: List[User] = UserQueries.list
    Ok(views.html.index(list))
  }

  def create = Action { implicit request =>
    val result: Int  = UserQueries.create(User("John Doe" ))
    Ok("status " + result)
  }

  def findById(id: String) = Action { implicit request =>

    Try(UUID.fromString(id)) match {
      case Success(result) =>
        val uuid = result.asInstanceOf[UUID]
        val user: Option[User] = UserQueries.getById(uuid)
        user.map(t => Ok(t.id.get +" - " + t.name )).getOrElse(NotFound)

      case failure@Failure(ex) => BadRequest("Id not found")
    }
  }
}

object Application extends Application
