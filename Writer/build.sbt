name := "Writer"

version := "1.0"

scalaVersion := "2.13.5"

lazy val akkaVersion         = "2.6.14"
lazy val configVersion       = "1.4.1"
lazy val cassandraVersion    = "1.0.5"
lazy val alpakkaKafkaVersion = "2.0.7"
lazy val slf4jVersion        = "1.7.30"

libraryDependencies ++= Seq(
  "com.typesafe"       % "config"                     % configVersion,
  "com.typesafe.akka" %% "akka-actor-typed"           % akkaVersion,
  "com.typesafe.akka" %% "akka-persistence-typed"     % akkaVersion,
  "com.typesafe.akka" %% "akka-persistence-cassandra" % cassandraVersion,
  "com.typesafe.akka" %% "akka-persistence"           % akkaVersion,
  "com.typesafe.akka" %% "akka-persistence-query"     % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster-tools"         % akkaVersion,
  "com.typesafe.akka" %% "akka-stream"                % akkaVersion,
  "com.typesafe.akka" %% "akka-stream-kafka"          % alpakkaKafkaVersion,
  "org.slf4j"          % "slf4j-nop"                  % slf4jVersion,
  "com.typesafe.akka" %% "akka-persistence-testkit"   % akkaVersion % Test
)
