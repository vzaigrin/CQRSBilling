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

class MonthlyRouter(pathPrefix: String, service: Service[Monthly], pool: ExecutorService)
    extends BaseRouter(pathPrefix: String, service: Service[Monthly], pool: ExecutorService) {
  private val pathSuffix: String = "monthly"

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
  def findAllEndpoint: Endpoint[Unit, ErrorInfo, Seq[Monthly], Any] =
    baseEndpoint.get
      .description("Вывод месячного отчёта для всех msisdn")
      .out(jsonBody[Seq[Monthly]])

  def findAll: Future[Either[ErrorInfo, Seq[Monthly]]] = find(Seq())

  def findAllRoute: Route = AkkaHttpServerInterpreter.toRoute(findAllEndpoint)(_ => findAll)

  // Выводим отчёты по параметрам
  def findByFieldEndpoint
      : Endpoint[(Option[String], Option[String], Option[String]), ErrorInfo, Seq[
        Monthly
      ], Any] =
    baseEndpoint.get
      .description("Вывод месячного отчёта по параметрам")
      .in("find")
      .in(query[Option[String]]("msisdn"))
      .in(query[Option[String]]("year"))
      .in(query[Option[String]]("month"))
      .out(jsonBody[Seq[Monthly]])

  def findByField(
      query: (Option[String], Option[String], Option[String])
  ): Future[Either[ErrorInfo, Seq[Monthly]]] = {
    val qSeq: Seq[(String, Option[String])] =
      Seq(("msisdn", query._1), ("year", query._2), ("month", query._3))
        .filterNot(_._2.isEmpty)
    find(qSeq)
  }

  def findByFieldRoute: Route = AkkaHttpServerInterpreter.toRoute(findByFieldEndpoint)(findByField)
}
