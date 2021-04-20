package ru.otus.scala.model

case class Monthly(
    msisdn: Long,
    year: Int,
    month: Int,
    call: Long,
    text: Long,
    web: Long
) extends Entity {
  override def toString: String =
    s"$msisdn\t$year\t$month,\tcall: $call\ttext: $text\tweb: $web"
}
