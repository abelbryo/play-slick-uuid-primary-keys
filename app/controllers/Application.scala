package controllers

import play.api._
import play.api.mvc._
import scala.util.{Try, Success, Failure}
import java.util.UUID

class Application extends Controller {

  import models._

  def index = Action {
    val users: List[User] = UserQueries.list
    val employees: List[Employee] = EmployeeManager.selectAll
    val students: List[Student] = StudentQueries.list

    Ok(views.html.index(users, employees, students))
  }

  def create = Action { implicit request =>
    val user = User(name = "John Doe" )
    val employee = Employee(None, "Johnny B Good")
    val student = Student(None, "Margaret Jackson")

    val result: Int  = UserQueries.create(user )
    val result2: Unit = EmployeeManager.insert(employee)
    val result3: Int = StudentQueries.create(student)

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
