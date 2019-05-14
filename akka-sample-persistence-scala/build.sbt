organization := "com.typesafe.akka.samples"
name := "akka-sample-persistence-scala"

scalaVersion := "2.12.8"
val AkkaVersion = "2.5.19"
val AkkaPersistenceCassandraVersion = "0.97"

libraryDependencies ++= Seq(
  "com.typesafe.akka"          %% "akka-persistence" % AkkaVersion,
  "com.typesafe.akka" %% "akka-persistence-cassandra" % AkkaPersistenceCassandraVersion,
  "com.typesafe.akka" %% "akka-persistence-cassandra-launcher" % AkkaPersistenceCassandraVersion
)

fork in run := false
mainClass in (Compile, run) := Some("sample.persistence.PersistentActorApp")

licenses := Seq(("CC0", url("http://creativecommons.org/publicdomain/zero/1.0")))