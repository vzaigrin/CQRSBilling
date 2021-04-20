package ru.otus.scala.dao

import ru.otus.scala.model.Entity
import scala.concurrent.Future

trait Dao[T <: Entity] {
  def get(msisdn: Long): Future[Option[T]]
  def findByField(field: String, value: String): Future[Seq[T]]
  def findAll(): Future[Seq[T]]
}
