package ru.otus.scala

import akka.Done
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.kafka.scaladsl.Consumer
import akka.kafka.{ConsumerSettings, Subscriptions}
import com.typesafe.config.ConfigFactory
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization._
import scala.concurrent.{ExecutionContext, Future}
import io.circe.generic.auto._
import io.circe.parser.decode

object Writer {
  def main(args: Array[String]): Unit = {

    // Читаем конфигурационный файл
    val config           = ConfigFactory.load()
    val bootstrapServers = config.getString("kafka.bootstrap.servers")
    val topics           = config.getString("kafka.topics").split(",")
    val groupId          = config.getString("kafka.group.id")

    // Создаём систему акторов
    implicit val system: ActorSystem[Nothing]       = ActorSystem(Behaviors.empty, "Writer")
    implicit val executionContext: ExecutionContext = system.executionContext

    // Consumer
    val consumerSettings =
      ConsumerSettings(system.classicSystem, new IntegerDeserializer, new StringDeserializer)
        .withBootstrapServers(bootstrapServers)
        .withGroupId(groupId)
        .withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")

    val done: Future[Done] = Consumer
      .plainSource(consumerSettings, Subscriptions.topics(topics: _*))
      .map { cr => (cr.topic, decode[Data](cr.value)) }
      .filter { cr => cr._2.isRight }
      .map { cr =>
        cr._2 match {
          case Right(v) => Event(cr._1, v)
        }
      }
      .runForeach(println)
//      .toMat(Sink.foreach(println))(Keep.right)
//      .run()

    done.onComplete(_ => system.terminate())
  }
}
