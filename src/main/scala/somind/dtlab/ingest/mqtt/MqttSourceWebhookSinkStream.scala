package somind.dtlab.ingest.mqtt

import akka.Done
import akka.http.scaladsl.model.StatusCodes
import akka.stream.alpakka.mqtt._
import akka.stream.alpakka.mqtt.scaladsl.{MqttMessageWithAck, MqttSource}
import akka.stream.scaladsl.Source
import com.typesafe.scalalogging.LazyLogging
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import somind.dtlab.ingest.mqtt.Conf._
import somind.dtlab.ingest.mqtt.observe.Observer
import somind.dtlab.ingest.mqtt.utils.{PostString, SslContextUtil}

import scala.concurrent.Future

object MqttSourceWebhookSinkStream extends LazyLogging {

  def apply(): Unit = {

    val connectionSettings = MqttConnectionSettings(
      mqttUrl,
      mqttClientId,
      new MemoryPersistence
    ).withAutomaticReconnect(true)
      .withSocketFactory(SslContextUtil().getSocketFactory)
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
      .runWith(akka.stream.scaladsl.Sink.foreach(m => {

        Observer("mqtt_message_processing_by_sink")
        val payload = m.message.payload.map(_.toChar).mkString
        logger.debug(payload)
        PostString(payload) match {
          case Some(code) if code == StatusCodes.Accepted =>
            m.ack()
            Observer("mqtt_message_processed_by_sink_fitness")
          case code =>
            m.ack() // todo: decide if this is a data issue - if data issue, log and ack - else log and crash
            val emsg = "mqtt_message_NOT_processed_by_sink"
            logger.error(s"$emsg: $code")
            Observer(emsg)
        }

      }))
  }

}
