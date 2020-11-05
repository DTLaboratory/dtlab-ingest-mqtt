package somind.dtlab.ingest.mqtt

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, ActorMaterializerSettings}
import com.typesafe.config.{Config, ConfigFactory}
import com.typesafe.scalalogging.LazyLogging

object Conf extends LazyLogging {

  InitJavaLogging()

  implicit val actorSystem: ActorSystem = ActorSystem("MqttToKafkaSystem")
  implicit val materializer: ActorMaterializer = ActorMaterializer(
    ActorMaterializerSettings(actorSystem))

  val conf: Config = ConfigFactory.load()
  val mqttUrl: String = conf.getString("main.mqttUrl")
  val mqttClientId: String = conf.getString("main.mqttClientId")
  val mqttTopic: String = conf.getString("main.mqttTopic")
  val keyStorePassword: String = conf.getString("main.keyStorePassword")
  val keyStorePath: String = conf.getString("main.keyStorePath")

}
