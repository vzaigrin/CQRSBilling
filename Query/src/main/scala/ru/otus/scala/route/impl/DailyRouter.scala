package ru.otus.scala.route.impl

import akka.http.scaladsl.server.Route
import sttp.tapir.server.akkahttp.AkkaHttpServerInterpreter
import akka.http.scaladsl.server.Directives.concat
import ru.otus.scala.model._
import ru.otus.scala.route._
import ru.otus.scala.service.Service
import sttp.tapir._
import sttp.tapir.json.circe._
import sttp.tapir.generic.auto._
import io.circe.generic.auto._
import java.util.concurrent.ExecutorService
import scala.concurrent.Future

class DailyRouter(pathPrefix: String, service: Service[Daily], pool: ExecutorService)
    extends BaseRouter(pathPrefix: String, service: Service[Daily], pool: ExecutorService) {
  private val pathSuffix: String = "daily"

  override def endpoints: List[Endpoint[_, _, _, _]] =
    List(
      findAllEndpoint,
      findByFieldEndpoint
    )

  override def route: Route = concat(findAllRoute, findByFieldRoute)

  private val baseEndpoint: Endpoint[Unit, ErrorInfo, Unit, Any] =
    startEndpoint
      .tag(pathSuffix)
      .in(pathSuffix)

  // Выводим отчёт для всех msisdn
  def findAllEndpoint: Endpoint[Unit, ErrorInfo, Seq[Daily], Any] =
    baseEndpoint.get
      .description("Вывод полного отчёта для всех msisdn")
      .out(jsonBody[Seq[Daily]])

  def findAll: Future[Either[ErrorInfo, Seq[Daily]]] = find(Seq())

  def findAllRoute: Route = AkkaHttpServerInterpreter.toRoute(findAllEndpoint)(_ => findAll)

  // Выводим отчёты по параметрам
  def findByFieldEndpoint
      : Endpoint[(Option[String], Option[String], Option[String], Option[String]), ErrorInfo, Seq[
        Daily
      ], Any] =
    baseEndpoint.get
      .description("Вывод полного отчёта по параметрам")
      .in("find")
      .in(query[Option[String]]("msisdn"))
      .in(query[Option[String]]("year"))
      .in(query[Option[String]]("month"))
      .in(query[Option[String]]("day"))
      .out(jsonBody[Seq[Daily]])

  def findByField(
      query: (Option[String], Option[String], Option[String], Option[String])
  ): Future[Either[ErrorInfo, Seq[Daily]]] = {
    val qSeq: Seq[(String, Option[String])] =
      Seq(("msisdn", query._1), ("year", query._2), ("month", query._3), ("day", query._4))
        .filterNot(_._2.isEmpty)
    find(qSeq)
  }

  def findByFieldRoute: Route = AkkaHttpServerInterpreter.toRoute(findByFieldEndpoint)(findByField)
}
