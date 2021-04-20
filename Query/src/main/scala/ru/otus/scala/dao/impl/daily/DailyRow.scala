package ru.otus.scala.dao.impl.daily

import ru.otus.scala.model.Daily

case class DailyRow(
    msisdn: Long,
    year: Int,
    month: Int,
    day: Int,
    call: Long,
    text: Long,
    web: Long
) {
  def toDaily: Daily = Daily(msisdn, year, month, day, call, text, web)
}

object DailyRow extends ((Long, Int, Int, Int, Long, Long, Long) => DailyRow) {
  def fromDaily(daily: Daily): DailyRow =
    DailyRow(daily.msisdn, daily.year, daily.month, daily.day, daily.call, daily.text, daily.web)
}
