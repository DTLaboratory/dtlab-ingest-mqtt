package somind.dtlab.ingest.mqtt

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.http.scaladsl.model.{ContentType, ContentTypes}
import akka.util.Timeout
import com.typesafe.config.{Config, ConfigFactory}
import com.typesafe.scalalogging.LazyLogging
import somind.dtlab.ingest.mqtt.observe.Observer
import somind.dtlab.ingest.mqtt.utils.InitJavaLogging

import scala.concurrent.ExecutionContextExecutor

object Conf extends LazyLogging {

  InitJavaLogging()

  implicit val actorSystem: ActorSystem = ActorSystem("DtlabIngestMqtt")
  implicit val executionContext: ExecutionContextExecutor =
    actorSystem.dispatcher
  //implicit val materializer: ActorMaterializer = ActorMaterializer(ActorMaterializerSettings(actorSystem))

  val conf: Config = ConfigFactory.load()
  val port: Int = conf.getInt("main.port")
  val mqttUrl: String = conf.getString("main.mqttUrl")
  val mqttClientId: String = conf.getString("main.mqttClientId")
  val mqttTopic: String = conf.getString("main.mqttTopic")
  val keyStorePassword: String = conf.getString("main.keyStorePassword")
  val keyStorePath: String = conf.getString("main.keyStorePath")
  val dtlabIngestUris: List[String] = conf.getString("main.dtlabIngestUris").split(' ').toList
  import scala.concurrent.duration._
  val webhookTimeoutSeconds: Duration =
    conf.getInt("main.webhookTimeoutSeconds").seconds

  val telemetryContentType: ContentType.NonBinary =
    conf.getString("main.telemetryContentType") match {
      case "csv"  => ContentTypes.`text/csv(UTF-8)`
      case "text" => ContentTypes.`text/plain(UTF-8)`
      case _      => ContentTypes.`application/json`
    }

  def requestDuration: Duration = {
    val t = "120 seconds"
    Duration(t)
  }
  implicit def requestTimeout: Timeout = {
    val d = requestDuration
    FiniteDuration(d.length, d.unit)
  }

  val healthToleranceSeconds: Int =
    conf.getString("main.healthToleranceSeconds").toInt
  val observer: ActorRef = actorSystem.actorOf(Props[Observer], "observer")

}
