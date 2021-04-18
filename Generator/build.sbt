name := "Generator"

version := "1.0"

scalaVersion := "2.13.5"

lazy val zioVersion       = "1.0.6"
lazy val zioConfig        = "1.0.4"
lazy val zioKafka         = "0.14.0"
lazy val zioLogging       = "0.5.8"
lazy val circeVersion     = "0.13.0"
lazy val log4jVersion     = "2.14.1"
lazy val disruptorVersion = "3.4.3"
lazy val jacksonVersion   = "2.12.3"

libraryDependencies ++= Seq(
  "dev.zio"                   %% "zio-config-magnolia" % zioConfig,
  "dev.zio"                   %% "zio-config-typesafe" % zioConfig,
  "dev.zio"                   %% "zio-streams"         % zioVersion,
  "dev.zio"                   %% "zio-kafka"           % zioKafka,
  "dev.zio"                   %% "zio-logging"         % zioLogging,
  "dev.zio"                   %% "zio-logging-slf4j"   % zioLogging,
  "io.circe"                  %% "circe-core"          % circeVersion,
  "io.circe"                  %% "circe-generic"       % circeVersion,
  "org.apache.logging.log4j"   % "log4j-core"          % log4jVersion,
  "org.apache.logging.log4j"   % "log4j-slf4j-impl"    % log4jVersion,
  "com.lmax"                   % "disruptor"           % disruptorVersion,
  "com.fasterxml.jackson.core" % "jackson-databind"    % jacksonVersion
)

assemblyMergeStrategy in assembly := {
  case m if m.toLowerCase.endsWith("manifest.mf") => MergeStrategy.discard
  case "module-info.class"                        => MergeStrategy.first
  case _                                          => MergeStrategy.first
}
