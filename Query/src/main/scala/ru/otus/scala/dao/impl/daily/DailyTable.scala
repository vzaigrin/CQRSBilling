package ru.otus.scala.dao.impl.daily

import slick.jdbc.PostgresProfile.api._

class DailyTable(tag: Tag) extends Table[DailyRow](tag, "daily") {
  def msisdn = column[String]("msisdn")
  def year   = column[Int]("year")
  def month  = column[Int]("month")
  def day    = column[Int]("day")
  def call   = column[Long]("call")
  def text   = column[Long]("text")
  def web    = column[Long]("web")

  def * = (msisdn, year, month, day, call, text, web) <> (DailyRow.tupled, DailyRow.unapply)
}
