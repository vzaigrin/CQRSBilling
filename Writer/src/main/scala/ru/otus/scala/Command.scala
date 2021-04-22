package ru.otus.scala

case class Command(
    command: String,
    msisdn: String,
    year: Int,
    month: Int,
    day: Int,
    value: Long
)

object Command {
  def apply(command: String, data: Data): Command =
    Command(command, data.msisdn, data.year, data.month, data.day, data.value)

  def apply(): Command = Command("", "", 0, 0, 0, 0)
}
