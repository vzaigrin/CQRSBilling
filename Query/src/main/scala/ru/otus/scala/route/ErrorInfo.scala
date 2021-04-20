package ru.otus.scala.route

sealed trait ErrorInfo
case class NotFound(what: String)   extends ErrorInfo
case class BadRequest(what: String) extends ErrorInfo
