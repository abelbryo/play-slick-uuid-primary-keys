import models._
import play.api._
import play.api.db.slick.{ Session => DBSession, _ }
import play.api.mvc._
import play.api.Play.current

import scala.slick.jdbc.meta.MTable

object Global extends GlobalSettings {
  override def onStart(app: Application) {
    Init.createTables
  }

  override def onStop(app: Application) {}

  object Init {

    // shard_1 is the schema in postgres. no big deal

    def createTables = DB.withSession { implicit session: DBSession =>
      // Gotta check both possibilities because in postgres,
      // the search_path can be set to public or shard_1
      // if it is set to public, we have to use `shard_1.employees`
      // if it is set to shard_1, we can use `employees`
      if (MTable.getTables("employees").list(session).isEmpty
        && MTable.getTables("shard_1.employees").list(session).isEmpty) EmployeeManager.createTable
    }
  }
}

