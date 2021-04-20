package ru.otus.scala.model

case class GetRequest(msisdn: Long)

sealed trait GetResponse
object GetResponse {
  case class Found[+T <: Entity](entity: T) extends GetResponse
  case class NotFound(msisdn: Long)         extends GetResponse
}
