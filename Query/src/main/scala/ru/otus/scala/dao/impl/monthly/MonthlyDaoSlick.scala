package ru.otus.scala.dao.impl.monthly

import ru.otus.scala.dao.Dao
import ru.otus.scala.model.Monthly
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._
import scala.concurrent.{ExecutionContext, Future}

class MonthlyDaoSlick(db: Database)(implicit ec: ExecutionContext) extends Dao[Monthly] {
  val monthlyTable = TableQuery[MonthlyTable]

  private def findByCondition(condition: MonthlyTable => Rep[Boolean]): Future[Seq[Monthly]] =
    db.run(monthlyTable.filter(condition).result)
      .map(_.map { monthlyRow => monthlyRow.toMonthly }.toSeq)

  override def findByField(field: String, value: String): Future[Seq[Monthly]] =
    field.toLowerCase match {
      case "msisdn" => findByCondition(_.msisdn === value.toLong)
      case "year"   => findByCondition(_.year === value.toInt)
      case "month"  => findByCondition(_.month === value.toInt)
      case _        => Future.successful(Seq())
    }

  override def findAll(): Future[Seq[Monthly]] = findByCondition(_ => true)
}
