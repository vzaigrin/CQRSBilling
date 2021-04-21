package ru.otus.scala

import akka.Done
import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.Behaviors
import akka.kafka.scaladsl.Consumer
import akka.kafka.{ConsumerSettings, Subscriptions}
import com.typesafe.config.ConfigFactory
import org.apache.kafka.common.serialization._
import scala.concurrent.{ExecutionContext, Future}

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

    val done: Future[Done] = Consumer
      .plainSource(consumerSettings, Subscriptions.topics(topics: _*))
      .runForeach { cr => println(f"${cr.topic}%4s\t${cr.offset}\t${cr.key}\t${cr.value}") }
//      .toMat(Sink.foreach(println))(Keep.right)
//      .run()

    done.onComplete(_ => system.terminate())
  }
}
