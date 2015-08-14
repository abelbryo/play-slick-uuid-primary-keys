package models

import play.api.db.slick._
import scala.slick.jdbc.GetResult
import scala.slick.jdbc.{ StaticQuery => Q }
import play.api.Play.current

case class Employee(name: String, id: Option[Long])

/*
 * Slick models using plain SQL with a custom id generator
 */

object EmployeeManager {

  implicit val employeeConverter = GetResult(r => { Employee(r.<<, r.<<) })

  // === Queries === //

  var createTableQuery = Q.updateNA("""
      CREATE TABLE shard_1.employees (
        name VARCHAR(50) not null,
        id bigint not null default shard_1.id_generator() primary key
      )
    """)

  val selectAllQuery = Q.queryNA[Employee](
    """
      SELECT * FROM shard_1.employees;
    """)

  // === Methods using the queries === //

  def createTable = DB.withSession { implicit session: Session =>
    createTableQuery.execute
  }

  def selectAll: List[Employee] = DB.withSession { implicit session: Session =>
    selectAllQuery.list
  }

  def insert(e: Employee) = DB.withSession { implicit session: Session =>
    (Q.u + "INSERT INTO shard_1.employees VALUES (" +? e.name + ")").execute
  }

}
