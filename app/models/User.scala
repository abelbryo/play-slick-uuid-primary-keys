package models

import java.util.UUID

import play.api.Play.current
import play.api.db.slick._
import slick.driver.JdbcProfile

case class User(name: String, id: Option[UUID]=Some(UUID.randomUUID))

import play.api.db.slick.Config.driver.simple._

class Users(tag: Tag) extends Table[User](tag, "users") {
  def name = column[String]("name", O.NotNull)
  def id = column[UUID]("id", O.PrimaryKey, O.DBType("UUID"))

  def * = (name, id.?) <> (User.tupled, User.unapply _)
}

object UserQueries {
  val users = TableQuery[Users]

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

  def list : List[User] = DB.withSession {
    implicit session: Session =>
      (for { u <- users} yield u).list
  }


}

