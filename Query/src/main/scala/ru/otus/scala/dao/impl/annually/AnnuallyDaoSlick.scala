package ru.otus.scala.dao.impl.annually

import ru.otus.scala.dao.Dao
import ru.otus.scala.model.Annually
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._
import scala.concurrent.{ExecutionContext, Future}

class AnnuallyDaoSlick(db: Database)(implicit ec: ExecutionContext) extends Dao[Annually] {
  val annuallyTable = TableQuery[AnnuallyTable]

  override def get(msisdn: Long): Future[Option[Annually]] = {
    val res = for {
      annually <- annuallyTable.filter(annually => annually.msisdn === msisdn).result.headOption
    } yield annually.map(_.toAnnually)
    db.run(res)
  }

  private def findByCondition(condition: AnnuallyTable => Rep[Boolean]): Future[Seq[Annually]] =
    db.run(annuallyTable.filter(condition).result)
      .map(_.map { annuallyRow => annuallyRow.toAnnually }.toSeq)

  override def findByField(field: String, value: String): Future[Seq[Annually]] =
    field.toLowerCase match {
      case "msisdn" => findByCondition(_.msisdn.toString.equals(value))
      case "year"   => findByCondition(_.year.toString.equals(value))
      case _        => Future.successful(Seq())
    }

  override def findAll(): Future[Seq[Annually]] = findByCondition(_ => true)
}
