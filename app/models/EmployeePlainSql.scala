package models

import play.api.db.slick._
import scala.slick.jdbc.GetResult
import scala.slick.jdbc.{ StaticQuery => Q }
import play.api.Play.current

case class Employee(id: Option[Long], name: String)

/*
 * Slick models using plain SQL with a custom id generator
 */

object EmployeeManager {

  implicit val employeeConverter = GetResult(r => Employee(r.<<, r.<<))

  // === Queries === //
  //

  val createIdGeneratorFunctionQuery = Q.updateNA(
    """create schema shard_1;
      |create sequence shard_1.global_id_sequence;
      |CREATE OR REPLACE FUNCTION shard_1.id_generator(OUT result bigint) AS $$
      |DECLARE
      |    our_epoch bigint := 1314220021721;
      |    seq_id bigint;
      |    now_millis bigint;
      |    -- the id of this DB shard, must be set for each
      |    -- schema shard you have - you could pass this as a parameter too
      |    shard_id int := 1;
      |
      |    BEGIN
      |        SELECT nextval('shard_1.global_id_sequence') % 1024 INTO seq_id;
      |
      |        SELECT FLOOR(EXTRACT(EPOCH FROM clock_timestamp()) * 1000) INTO now_millis;
      |        result := (now_millis - our_epoch) << 23;
      |        result := result | (shard_id << 10);
      |        result := result | (seq_id);
      |    END;
      |$$ LANGUAGE PLPGSQL;
      |
      |SELECT shard_1.id_generator();""".stripMargin
  )

  val createTableQuery = Q.updateNA(
    """CREATE TABLE shard_1.employees (
      |   id bigint not null default shard_1.id_generator() primary key,
      |   name VARCHAR(50) not null
      |)""".stripMargin
  )

  val selectAllQuery = Q.queryNA[Employee]("""SELECT * FROM shard_1.employees;""")

  // === Methods using the queries === //

  def createIdGeneratorFunction = DB.withSession { implicit session: Session =>
    createIdGeneratorFunctionQuery.execute
  }

  def createTable = DB.withSession { implicit session: Session =>
    createTableQuery.execute
  }

  def selectAll: List[Employee] = DB.withSession { implicit session: Session =>
    selectAllQuery.list
  }

  def insert(e: Employee) = DB.withSession { implicit session: Session =>
    (Q.u + "INSERT INTO shard_1.employees (name) VALUES (" +? e.name + ")").execute
  }

}
