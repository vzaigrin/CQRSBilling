package ru.otus.scala

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import com.typesafe.config.ConfigFactory
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import ru.otus.scala.dao.impl.annually.AnnuallyDaoSlick
import ru.otus.scala.dao.impl.daily.DailyDaoSlick
import ru.otus.scala.dao.impl.monthly.MonthlyDaoSlick
import ru.otus.scala.route.impl._
import ru.otus.scala.service.impl.ServiceImpl
import slick.jdbc.JdbcBackend.Database
import sttp.tapir.openapi.OpenAPI
import sttp.tapir.openapi.circe.yaml._
import sttp.tapir.docs.openapi._
import sttp.tapir.swagger.akkahttp.SwaggerAkka
import java.util.concurrent.Executors
import scala.concurrent.ExecutionContext
import scala.io.StdIn
import scala.util.Using

object Query {
  def main(args: Array[String]): Unit = {
    // Читаем конфигурационный файл
    val config = ConfigFactory.load()

    // Адрес и порт для HTTP сервера
    val host: String       = config.getString("http.host")
    val port: Int          = config.getInt("http.port")
    val pathPrefix: String = config.getString("pathPrefix")

    implicit val actorSystem: ActorSystem = ActorSystem("system")
    val pool                              = Executors.newCachedThreadPool()

    // Подключаемся к базе по конфигурации из файла
    Using.resource(Database.forConfig("db")) { db =>
      implicit val ec: ExecutionContext = db.executor.executionContext

      // Создаём DAO
      val dailyDao    = new DailyDaoSlick(db)
      val monthlyDao  = new MonthlyDaoSlick(db)
      val annuallyDao = new AnnuallyDaoSlick(db)

      // Создаём Routers
      val routers = List(
        new DailyRouter(pathPrefix, new ServiceImpl(dailyDao), pool),
        new MonthlyRouter(pathPrefix, new ServiceImpl(monthlyDao), pool),
        new AnnuallyRouter(pathPrefix, new ServiceImpl(annuallyDao), pool)
      )

      val endpoints            = routers.flatMap(_.endpoints)
      val openApiDocs: OpenAPI = OpenAPIDocsInterpreter.toOpenAPI(endpoints, "Billing", "1.0.0")
      val openApiYml: String   = openApiDocs.toYaml

      val route: Route =
        concat(routers.map(_.route) ::: List(new SwaggerAkka(openApiYml).routes): _*)

      val bindingFuture = Http()
        .newServerAt(host, port)
        .bind(route)

      println(s"Server online at https://$host:$port/")
      println(s"Docs at: https://$host:$port/docs")
      println("Press any key to exit ...")
      StdIn.readLine() // let it run until user presses return

      bindingFuture
        .flatMap(_.unbind())                      // trigger unbinding from the port
        .onComplete(_ => actorSystem.terminate()) // and shutdown when done
    }
  }
}
