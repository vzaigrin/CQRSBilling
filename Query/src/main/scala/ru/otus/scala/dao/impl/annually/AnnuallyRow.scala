package ru.otus.scala.dao.impl.annually

import ru.otus.scala.model.Annually

case class AnnuallyRow(
    msisdn: String,
    year: Int,
    call: Long,
    text: Long,
    web: Long
) {
  def toAnnually: Annually = Annually(msisdn, year, call, text, web)
}

object AnnuallyRow extends ((String, Int, Long, Long, Long) => AnnuallyRow) {
  def fromAnnually(annually: Annually): AnnuallyRow =
    AnnuallyRow(annually.msisdn, annually.year, annually.call, annually.text, annually.web)
}
