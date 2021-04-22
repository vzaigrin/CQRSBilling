package ru.otus.scala.service.impl

import ru.otus.scala.dao.Dao
import ru.otus.scala.model._
import ru.otus.scala.service.Service
import scala.concurrent.{ExecutionContext, Future}

class ServiceImpl[T <: Entity](dao: Dao[T])(implicit ec: ExecutionContext) extends Service[T] {
  override def find(request: FindRequest): Future[FindResponse] =
    request match {
      case FindRequest.ByField(field, value) =>
        dao.findByField(field, value).map(r => FindResponse.Result(r))
      case FindRequest.All => dao.findAll().map(r => FindResponse.Result(r))
    }
}
