package ru.otus.scala.dao.impl.monthly

import ru.otus.scala.dao.Dao
import ru.otus.scala.model.Monthly
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._
import scala.concurrent.{ExecutionContext, Future}

class MonthlyDaoSlick(db: Database)(implicit ec: ExecutionContext) extends Dao[Monthly] {
  val monthlyTable = TableQuery[MonthlyTable]

  override def get(msisdn: Long): Future[Option[Monthly]] = {
    val res = for {
      monthly <- monthlyTable.filter(monthly => monthly.msisdn === msisdn).result.headOption
    } yield monthly.map(_.toMonthly)
    db.run(res)
  }

  private def findByCondition(condition: MonthlyTable => Rep[Boolean]): Future[Seq[Monthly]] =
    db.run(monthlyTable.filter(condition).result)
      .map(_.map { monthlyRow => monthlyRow.toMonthly }.toSeq)

  override def findByField(field: String, value: String): Future[Seq[Monthly]] =
    field.toLowerCase match {
      case "msisdn" => findByCondition(_.msisdn.toString.equals(value))
      case "year"   => findByCondition(_.year.toString.equals(value))
      case "month"  => findByCondition(_.month.toString.equals(value))
      case _        => Future.successful(Seq())
    }

  override def findAll(): Future[Seq[Monthly]] = findByCondition(_ => true)
}
