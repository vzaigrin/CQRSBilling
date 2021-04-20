package ru.otus.scala

import zio._
import zio.stream._
import zio.blocking._
import zio.kafka.serde._
import zio.kafka.producer._
import zio.logging._
import zio.logging.slf4j._
import io.circe.syntax.EncoderOps
import org.apache.kafka.clients.producer.ProducerRecord
import scala.util.Random

object Generator extends App {
  type ProducerEnv = Any with Blocking with Producer[Any, Int, String] with Logging
  val rnd = new Random

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] =
    AppConfig
      .load()
      .flatMap(config => program(config).provideSomeLayer[Any with Blocking](createLayer(config)))
      .exitCode

  private def program(appConfig: AppConfig): ZIO[ProducerEnv, Throwable, Unit] =
    ZStream
      .fromIterable {
        (1 to appConfig.recordsNumber)
          .flatMap { id =>
            val msisdn = appConfig.msisdnBase + "%02d".format(rnd.nextInt(appConfig.msisdnNumber))
            val year   = appConfig.yearsList(rnd.nextInt(appConfig.yearsList.length))
            val month  = appConfig.monthsList(rnd.nextInt(appConfig.monthsList.length))
            val day    = appConfig.daysList(rnd.nextInt(appConfig.daysList.length))
            val calls  = rnd.nextInt(60)
            val text   = rnd.nextInt(10)
            val web    = rnd.nextInt(100)
            List(
              new ProducerRecord(
                appConfig.callTopic,
                id,
                Data(msisdn, year, month, day, calls).asJson.toString
              ),
              new ProducerRecord(
                appConfig.textTopic,
                id,
                Data(msisdn, year, month, day, text).asJson.toString
              ),
              new ProducerRecord(
                appConfig.webTopic,
                id,
                Data(msisdn, year, month, day, web).asJson.toString
              )
            )
          }
      }
      .mapM { producerRecord =>
        log.info(s"Producing $producerRecord") *>
          Producer.produce[Any, Int, String](producerRecord)
      }
      .runDrain

  private def createLayer(
      appConfig: AppConfig
  ): ZLayer[Any, Throwable, Logging with Has[Producer.Service[Any, Int, String]]] = {
    val producerSettings = ProducerSettings(appConfig.brokers)
    val producerLayer =
      Producer.make[Any, Int, String](producerSettings, Serde.int, Serde.string).toLayer

    val loggingLayer = Slf4jLogger.make { (_, message) => message }

    loggingLayer ++ producerLayer
  }
}
