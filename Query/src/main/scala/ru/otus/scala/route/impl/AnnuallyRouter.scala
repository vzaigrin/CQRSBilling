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

class AnnuallyRouter(pathPrefix: String, service: Service[Annually], pool: ExecutorService)
    extends BaseRouter(pathPrefix: String, service: Service[Annually], pool: ExecutorService) {
  private val pathSuffix: String = "annually"

  override def endpoints: List[Endpoint[_, _, _, _]] =
    List(
      getEndpoint,
      findAllEndpoint,
      findByFieldEndpoint
    )

  override def route: Route = concat(getRoute, findAllRoute, findByFieldRoute)

  private val baseEndpoint: Endpoint[Unit, ErrorInfo, Unit, Any] =
    startEndpoint
      .tag(pathSuffix)
      .in(pathSuffix)

  // Выводим отчёт для msisdn
  def getEndpoint: Endpoint[Long, ErrorInfo, Annually, Any] =
    baseEndpoint.get
      .description("Вывод отчёта для msisdn")
      .in(path[Long])
      .out(jsonBody[Annually])

  def getRoute: Route = AkkaHttpServerInterpreter.toRoute(getEndpoint)(get)

  // Выводим отчёт для всех msisdn
  def findAllEndpoint: Endpoint[Unit, ErrorInfo, Seq[Annually], Any] =
    baseEndpoint
      .description("Вывод отчёта всех msisdn")
      .out(jsonBody[Seq[Annually]])

  def findAll: Future[Either[ErrorInfo, Seq[Annually]]] = find(Seq())

  def findAllRoute: Route = AkkaHttpServerInterpreter.toRoute(findAllEndpoint)(_ => findAll)

  // Выводим отчёты по параметрам
  def findByFieldEndpoint: Endpoint[(Option[String], Option[String]), ErrorInfo, Seq[
    Annually
  ], Any] =
    baseEndpoint.get
      .description("Вывод отчётов по параметрам")
      .in("find")
      .in(query[Option[String]]("msisdn"))
      .in(query[Option[String]]("year"))
      .out(jsonBody[Seq[Annually]])

  def findByField(
      query: (Option[String], Option[String])
  ): Future[Either[ErrorInfo, Seq[Annually]]] = {
    val qSeq: Seq[(String, Option[String])] =
      Seq(("msisdn", query._1), ("year", query._2))
        .filterNot(_._2.isEmpty)
    find(qSeq)
  }

  def findByFieldRoute: Route = AkkaHttpServerInterpreter.toRoute(findByFieldEndpoint)(findByField)
}
