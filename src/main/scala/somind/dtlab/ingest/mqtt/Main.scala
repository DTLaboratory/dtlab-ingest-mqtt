package somind.dtlab.ingest.mqtt

import akka.Done
import akka.stream.alpakka.mqtt._
import akka.stream.alpakka.mqtt.scaladsl.{MqttMessageWithAck, MqttSource}
import akka.stream.scaladsl.Source
import com.typesafe.scalalogging.LazyLogging
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import Conf._

import scala.concurrent.Future

object Main extends App with LazyLogging {

  val sslContext = SslContextUtil()

  val connectionSettings = MqttConnectionSettings(
    mqttUrl,
    mqttClientId,
    new MemoryPersistence
  ).withAutomaticReconnect(true)
    .withServerUri(mqttUrl)
    .withSocketFactory(sslContext.getSocketFactory)
    .withCleanSession(false)

  logger.debug(s"connection settings:\n$connectionSettings")

  val src: Source[MqttMessageWithAck, Future[Done]] =
    MqttSource.atLeastOnce(
      connectionSettings.withClientId(
        clientId = "somind-mqtt-gateway-" + System.nanoTime().toString),
      MqttSubscriptions(mqttTopic, MqttQoS.AtLeastOnce),
      bufferSize = 8
    )

  logger.info("starting...")
  logger.debug("debugging...")
  src
    .map(m => {
      m.ack()
      m.message.payload.map(_.toChar).mkString
    })
    .runWith(akka.stream.scaladsl.Sink.foreach(println))

}
