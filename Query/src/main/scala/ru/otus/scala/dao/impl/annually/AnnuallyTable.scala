package ru.otus.scala.dao.impl.annually

import slick.jdbc.PostgresProfile.api._

class AnnuallyTable(tag: Tag) extends Table[AnnuallyRow](tag, "annually") {
  def msisdn = column[Long]("msisdn", O.PrimaryKey)
  def year   = column[Int]("year")
  def call   = column[Long]("call")
  def text   = column[Long]("text")
  def web    = column[Long]("web")

  def * = (msisdn, year, call, text, web) <> (AnnuallyRow.tupled, AnnuallyRow.unapply)
}
