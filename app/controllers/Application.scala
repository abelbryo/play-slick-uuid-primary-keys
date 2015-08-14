package controllers

import play.api._
import play.api.mvc._
import scala.util.{Try, Success, Failure}
import java.util.UUID

class Application extends Controller {

  import models._

  def index = Action {
    val list: List[User] = UserQueries.list
    val employeeList: List[Employee] = EmployeeManager.selectAll

    Ok(views.html.index(list, employeeList))
  }

  def create = Action { implicit request =>
    val result: Int  = UserQueries.create(User("John Doe" ))
    val employee = Employee("Johnny B Good", None)

    EmployeeManager.insert(employee)


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
