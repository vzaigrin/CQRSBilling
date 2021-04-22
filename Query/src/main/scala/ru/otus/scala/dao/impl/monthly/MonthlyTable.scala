package ru.otus.scala.dao.impl.monthly

import slick.jdbc.PostgresProfile.api._

class MonthlyTable(tag: Tag) extends Table[MonthlyRow](tag, "monthly") {
  def msisdn = column[Long]("msisdn")
  def year   = column[Int]("year")
  def month  = column[Int]("month")
  def call   = column[Long]("call")
  def text   = column[Long]("text")
  def web    = column[Long]("web")

  def * = (msisdn, year, month, call, text, web) <> (MonthlyRow.tupled, MonthlyRow.unapply)
}
