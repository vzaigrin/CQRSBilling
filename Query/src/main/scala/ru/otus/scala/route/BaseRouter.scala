package ru.otus.scala.route

import ru.otus.scala.model._
import ru.otus.scala.service.Service
import akka.http.scaladsl.server.Route
import scala.concurrent.{ExecutionContext, ExecutionContextExecutor, Future}
import sttp.tapir._
import sttp.tapir.{Endpoint, endpoint}
import sttp.tapir.json.circe._
import sttp.tapir.generic.auto._
import io.circe.generic.auto._
import java.util.concurrent.ExecutorService

abstract class BaseRouter[T <: Entity](
    pathPrefix: String,
    service: Service[T],
    pool: ExecutorService
) {
  implicit val ec: ExecutionContextExecutor = ExecutionContext.fromExecutor(pool)

  def endpoints: List[Endpoint[_, _, _, _]]
  def route: Route

  val startEndpoint: Endpoint[Unit, ErrorInfo, Unit, Any] =
    endpoint
      .in(pathPrefix)
      .errorOut(jsonBody[ErrorInfo])

  def get(msisdn: Long): Future[Either[ErrorInfo, T]] = {
    service.get(GetRequest(msisdn)) map {
      case GetResponse.Found(e)     => Right(e.asInstanceOf[T])
      case GetResponse.NotFound(id) => Left(NotFound(id.toString))
    }
  }

  def find(qSeq: Seq[(String, Option[String])]): Future[Either[ErrorInfo, Seq[T]]] = {
    qSeq.headOption match {
      case Some(key) =>
        service.find(FindRequest.ByField(key._1, key._2.get)) map {
          case FindResponse.Result(e) => Right(e.map(_.asInstanceOf[T]))
          case _                      => Left(NotFound(key._1))
        }
      case None =>
        service.find(FindRequest.All) map {
          case FindResponse.Result(e) => Right(e.map(_.asInstanceOf[T]))
          case _                      => Left(NotFound("all"))
        }
    }
  }
}
