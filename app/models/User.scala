package models

import java.util.UUID

import play.api.Play.current
import play.api.db.slick._
import slick.driver.JdbcProfile
import scala.slick.jdbc.meta.MTable

case class User(id: Option[UUID] = Some(UUID.randomUUID), name: String )

import play.api.db.slick.Config.driver.simple._

class Users(tag: Tag) extends Table[User](tag, Some("shard_1"), "users") {
  def id = column[UUID]("id", O.PrimaryKey, O.DBType("UUID"))
  def name = column[String]("name", O.NotNull)

  def * = (id.?, name) <> (User.tupled, User.unapply _)
}

object UserQueries {
  val users = TableQuery[Users]

  def createTable()(implicit session: Session) {
    if (MTable.getTables("users").list(session).isEmpty) {
      users.ddl.create
    }
  }

  def create(user: User): Int = DB.withSession {
    implicit session: Session =>
      users.insert(user)
  }

  def delete(id: UUID): Int = DB.withSession {
    implicit session: Session =>
      users.filter(_.id === id).delete
  }

  def getById(id: UUID): Option[User] = DB.withSession {
    implicit session: Session =>
      users.filter(_.id === id).firstOption
  }

  def list: List[User] = DB.withSession {
    implicit session: Session =>
      (for { u <- users } yield u).list
  }

}

