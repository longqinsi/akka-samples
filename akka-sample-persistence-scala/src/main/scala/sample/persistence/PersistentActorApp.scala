package sample.persistence

import java.io.File
import java.util.concurrent.CountDownLatch

import akka.persistence.cassandra.testkit.CassandraLauncher
import com.typesafe.config.{Config, ConfigFactory}

import scala.collection.breakOut

object PersistentActorApp {

  private val Opt = """(\S+)=(\S+)""".r

  private def argsToOpts(args: Seq[String]): Map[String, String] =
    args.collect { case Opt(key, value) => key -> value }(breakOut)

  private def applySystemProperties(options: Map[String, String]): Unit =
    for ((key, value) <- options if key startsWith "-D")
      System.setProperty(key.substring(2), value)

  def main(args: Array[String]): Unit = {

    val opts = argsToOpts(args.toList)
    applySystemProperties(opts)

    args.headOption match {

      case Some("cassandra") =>
        startCassandraDatabase()
        println("Started Cassandra, press Ctrl + C to kill")
        new CountDownLatch(1).await()

      case _ =>

    }
  }

  def config(port: Int): Config =
    ConfigFactory.parseString(
      s"""akka.remote.artery.canonical.port = $port
          akka.remote.netty.tcp.port = $port
       """).withFallback(ConfigFactory.load("application.conf"))

  /**
   * To make the sample easier to run we kickstart a Cassandra instance to
   * act as the journal. Cassandra is a great choice of backend for Akka Persistence but
   * in a real application a pre-existing Cassandra cluster should be used.
   */
  def startCassandraDatabase(): Unit = {
    val databaseDirectory = new File("target/cassandra-db")
    CassandraLauncher.start(
      databaseDirectory,
      CassandraLauncher.DefaultTestConfigResource,
      clean = true,
      port = 9042)
  }

}
