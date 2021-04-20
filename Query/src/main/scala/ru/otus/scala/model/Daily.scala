package ru.otus.scala.model

case class Daily(
    msisdn: Long,
    year: Int,
    month: Int,
    day: Int,
    call: Long,
    text: Long,
    web: Long
) extends Entity {
  override def toString: String =
    s"$msisdn\t$year\t$month,\t$day\tcall: $call\ttext: $text\tweb: $web"
}
