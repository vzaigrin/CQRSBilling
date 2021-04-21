package ru.otus.scala

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.kafka.scaladsl.Consumer
import akka.kafka.{ConsumerSettings, Subscriptions}
import akka.stream.scaladsl._
import com.typesafe.config.ConfigFactory
import org.apache.kafka.common.serialization._
import scala.concurrent.ExecutionContext

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

    Consumer
      .plainSource(consumerSettings, Subscriptions.topics(topics: _*))
      .toMat(Sink.foreach(println))(Keep.right)
      .run()

//    system.terminate()
  }
}
