package somind.dtlab.ingest.mqtt

import akka.actor.ActorSystem
import akka.stream._
import com.typesafe.config.{Config, ConfigFactory}
import com.typesafe.scalalogging.LazyLogging

object Conf extends Conf with LazyLogging {

  implicit val actorSystem: ActorSystem = ActorSystem("MqttKafka")

  val decider: Supervision.Decider = { e: Throwable =>
    logger.error(s"decider can not decide: $e - restarting...", e)
    Supervision.Restart
  }

  implicit val materializer: ActorMaterializer = ActorMaterializer(
    ActorMaterializerSettings(actorSystem).withSupervisionStrategy(decider))
}

trait Conf {

  val conf: Config = ConfigFactory.load()

  val mqttClientId: String = conf.getString("mqtt.clientId")
  val mqttUrl: String = conf.getString("mqtt.url")
  val mqttUser: String = conf.getString("mqtt.user")
  val mqttPwd: String = conf.getString("mqtt.pwd")
  val mqttTopic: String = conf.getString("mqtt.topic")

}
