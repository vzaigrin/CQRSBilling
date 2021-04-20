package ru.otus.scala.model

trait FindRequest
object FindRequest {
  case class ByField(field: String, value: String) extends FindRequest
  case object All                                  extends FindRequest
}

sealed trait FindResponse
object FindResponse {
  case class Result[+T <: Entity](entities: Seq[T]) extends FindResponse
}
