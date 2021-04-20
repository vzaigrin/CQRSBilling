package ru.otus.scala.dao.impl.monthly

import ru.otus.scala.model.Monthly

case class MonthlyRow(
    msisdn: Long,
    year: Int,
    month: Int,
    call: Long,
    text: Long,
    web: Long
) {
  def toMonthly: Monthly = Monthly(msisdn, year, month, call, text, web)
}

object MonthlyRow extends ((Long, Int, Int, Long, Long, Long) => MonthlyRow) {
  def fromMonthly(monthly: Monthly): MonthlyRow =
    MonthlyRow(monthly.msisdn, monthly.year, monthly.month, monthly.call, monthly.text, monthly.web)
}
