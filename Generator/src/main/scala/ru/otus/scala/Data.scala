package ru.otus.scala

import io.circe.Codec
import io.circe.generic.semiauto._

case class Data(msisdn: String, year: Int, month: Int, day: Int, value: Int)

object Data {
  implicit val codec: Codec[Data] = deriveCodec[Data]
}
