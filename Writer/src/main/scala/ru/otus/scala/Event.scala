package ru.otus.scala

case class Event(
    eventType: String,
    msisdn: String,
    year: Int,
    month: Int,
    day: Int,
    value: Long
)

object Event {
  def apply(eventType: String, data: Data): Event =
    Event(eventType, data.msisdn, data.year, data.month, data.day, data.value)
}
