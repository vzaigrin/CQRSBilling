package ru.otus.scala.model

case class Annually(msisdn: Long, year: Int, call: Long, text: Long, web: Long) extends Entity {
  override def toString: String =
    s"$msisdn\t$year,\tcall: $call\ttext: $text\tweb: $web"
}
