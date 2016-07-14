
package models

import java.util.UUID

import play.api.Play.current
import play.api.db.slick._
import slick.driver.JdbcProfile
import scala.slick.jdbc.meta.MTable

case class Student(id: Option[Long],  name: String )

import play.api.db.slick.Config.driver.simple._

class Students(tag: Tag) extends Table[Student](tag, Some("shard_1"), "students") {
  def id   = column[Option[Long]]("id", O.AutoInc, O.DBType("""bigint not null default shard_1.id_generator() primary key""".stripMargin))
  def name = column[String]("name", O.NotNull)

  def * = (id, name) <> (Student.tupled, Student.unapply _)
}

object StudentQueries {
  val students = TableQuery[Students]

  def createTable()(implicit session: Session) {
    if (MTable.getTables("students").list(session).isEmpty) {
      students.ddl.create
    }
  }

  def create(student: Student): Int = DB.withSession {
    implicit session: Session =>
      students.insert(student)
  }

  def delete(id: Long): Int = DB.withSession {
    implicit session: Session =>
      students.filter(_.id === id).delete
  }

  def getById(id: Long): Option[Student] = DB.withSession {
    implicit session: Session =>
      students.filter(_.id === id).firstOption
  }

  def list: List[Student] = DB.withSession {
    implicit session: Session =>
      (for { u <- students } yield u).list
  }

}

