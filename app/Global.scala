import models._
import play.api._
import play.api.db.slick.{ Session => DBSession, _ }
import play.api.mvc._
import play.api.Play.current

import scala.slick.jdbc.meta.MTable
import scala.slick.jdbc.meta.MFunction
import scala.slick.jdbc.meta.MProcedure
import scala.slick.jdbc.meta.MQName

object Global extends GlobalSettings {
  override def onStart(app: Application) {
    Init.createTables
  }

  override def onStop(app: Application) {}

  object Init {

    def createTables = DB.withSession { implicit session: DBSession =>

      /**
       * Checking if the id_generator procedure already exists.
       */
      if (MProcedure.getProcedures(new MQName(catalog = None, schema = Some("shard_1"), name = "id_generator")).list.isEmpty)
        EmployeeManager.createIdGeneratorFunction

      // Gotta check both possibilities because in postgres,
      // the search_path can be set to public or shard_1
      // if it is set to public, we have to use `shard_1.employees`
      // if it is set to shard_1, we can use `employees`
      if (MTable.getTables("employees").list(session).isEmpty
        && MTable.getTables("shard_1.employees").list(session).isEmpty) EmployeeManager.createTable

      // println(functions)

      if (MTable.getTables("users").list(session).isEmpty
        && MTable.getTables("shard_1.users").list(session).isEmpty) UserQueries.createTable


      if (MTable.getTables("students").list(session).isEmpty
        && MTable.getTables("shard_1.students").list(session).isEmpty) StudentQueries.createTable
    }
  }
}

