package ru.otus.scala

import zio._
import zio.config.read
import zio.config.magnolia.DeriveConfigDescriptor
import zio.config.typesafe.TypesafeConfigSource
import com.typesafe.config.ConfigFactory

final case class AppConfig(
    bootstrapServers: String,
    callTopic: String,
    textTopic: String,
    webTopic: String,
    msisdnBase: String,
    msisdnNumber: Int,
    recordsNumber: Int,
    years: String,
    months: String,
    days: String
) {
  def brokers: List[String]  = bootstrapServers.split(",").toList
  def yearsList: Array[Int]  = years.split(",").map(_.toInt)
  def monthsList: Array[Int] = months.split(",").map(_.toInt)
  def daysList: Array[Int]   = days.split(",").map(_.toInt)
}

object AppConfig {
  private val descriptor = DeriveConfigDescriptor.descriptor[AppConfig]

  def load(): Task[AppConfig] =
    for {
      rawConfig    <- ZIO.effect(ConfigFactory.load().getConfig("producer"))
      configSource <- ZIO.fromEither(TypesafeConfigSource.fromTypesafeConfig(rawConfig))
      config       <- ZIO.fromEither(read(AppConfig.descriptor.from(configSource)))
    } yield config
}
