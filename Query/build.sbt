name := "Query"

version := "1.0"

scalaVersion := "2.13.5"

lazy val akkaVersion     = "2.6.14"
lazy val akkaHttpVersion = "10.2.4"
lazy val tapirVersion    = "0.18.0-M5"
lazy val circeVersion    = "0.13.0"
lazy val slickVersion    = "3.3.3"
lazy val postgreVersion  = "42.2.19"
lazy val slf4jVersion    = "1.7.30"
lazy val jwtVersion      = "5.0.0"
lazy val configVersion   = "1.4.1"

libraryDependencies ++= Seq(
  "com.typesafe"                 % "config"                     % configVersion,
  "com.typesafe.akka"           %% "akka-actor-typed"           % akkaVersion,
  "com.typesafe.akka"           %% "akka-stream"                % akkaVersion,
  "com.typesafe.akka"           %% "akka-http"                  % akkaHttpVersion,
  "com.typesafe.slick"          %% "slick"                      % slickVersion,
  "com.typesafe.slick"          %% "slick-hikaricp"             % slickVersion,
  "org.slf4j"                    % "slf4j-nop"                  % slf4jVersion,
  "org.postgresql"               % "postgresql"                 % postgreVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-core"                 % tapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-akka-http-server"     % tapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-json-circe"           % tapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs"         % tapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-openapi-circe-yaml"   % tapirVersion,
  "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-akka-http" % tapirVersion,
  "io.circe"                    %% "circe-core"                 % circeVersion,
  "io.circe"                    %% "circe-generic"              % circeVersion,
  "io.circe"                    %% "circe-parser"               % circeVersion,
  "com.pauldijou"               %% "jwt-circe"                  % jwtVersion
//  "org.scalatest"               %% "scalatest"                  % "3.2.0"         % Test,
//  "org.scalamock"               %% "scalamock"                  % "5.0.0"         % Test,
//  "org.scalacheck"              %% "scalacheck"                 % "1.14.3"        % Test,
//  "org.scalatestplus"           %% "scalacheck-1-14"            % "3.2.0.0"       % Test,
//  "com.github.alexarchambault"  %% "scalacheck-shapeless_1.14"  % "1.2.3"         % Test
)
