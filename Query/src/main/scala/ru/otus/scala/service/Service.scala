package ru.otus.scala.service

import ru.otus.scala.model._
import scala.concurrent.Future

trait Service[T <: Entity] {
  def get(request: GetRequest): Future[GetResponse]
  def find(request: FindRequest): Future[FindResponse]
}
