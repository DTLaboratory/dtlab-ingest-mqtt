package somind.dtlab.ingest.mqtt

import akka.http.scaladsl.Http
import com.typesafe.scalalogging.LazyLogging
import somind.dtlab.ingest.mqtt.Conf._
import somind.dtlab.ingest.mqtt.observe.ObserverRoute

object Main extends App with LazyLogging {

  MqttSourceWebhookSinkStream()

  val route =
    ObserverRoute.apply

  Http().newServerAt("0.0.0.0", port).bindFlow(route)

}
