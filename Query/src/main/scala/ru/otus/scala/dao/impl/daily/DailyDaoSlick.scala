package ru.otus.scala.dao.impl.daily

import ru.otus.scala.dao.Dao
import ru.otus.scala.model.Daily
import slick.jdbc.JdbcBackend.Database
import slick.jdbc.PostgresProfile.api._
import scala.concurrent.{ExecutionContext, Future}

class DailyDaoSlick(db: Database)(implicit ec: ExecutionContext) extends Dao[Daily] {
  val dailyTable = TableQuery[DailyTable]

  private def findByCondition(condition: DailyTable => Rep[Boolean]): Future[Seq[Daily]] =
    db.run(dailyTable.filter(condition).result)
      .map(_.map { dailyRow => dailyRow.toDaily }.toSeq)

  override def findByField(field: String, value: String): Future[Seq[Daily]] =
    field.toLowerCase match {
      case "msisdn" => findByCondition(_.msisdn === value.toLong)
      case "year"   => findByCondition(_.year === value.toInt)
      case "month"  => findByCondition(_.month === value.toInt)
      case "day"    => findByCondition(_.day === value.toInt)
      case _        => Future.successful(Seq())
    }

  override def findAll(): Future[Seq[Daily]] = findByCondition(_ => true)
}
