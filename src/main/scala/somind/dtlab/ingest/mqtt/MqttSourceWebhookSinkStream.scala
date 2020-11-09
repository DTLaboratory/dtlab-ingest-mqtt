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

import scala.concurrent.{Await, Future, TimeoutException}

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
        try {

          Observer("mqtt_message_processing_by_sink")
          val payload = m.message.payload.map(_.toChar).mkString
          logger.debug(payload)
          import scala.concurrent.duration._
          val f = PostString(payload)
          Await.result(f, 10.seconds) match {
            case r if r.status == StatusCodes.Accepted =>
              m.ack()
              Observer("mqtt_message_processed_by_sink")
            case badResponse =>
              m.ack() // todo: decide if this is a data issue - if data issue, log and ack - else log and crash
              val emsg = "mqtt_message_NOT_processed_by_sink"
              logger.error(
                s"$emsg: ${badResponse.status} ${badResponse.entity}")
              Observer(emsg)
          }
        } catch {
          case e: TimeoutException =>
            logger.error(
              s"remote dtlab ingest system is timing out: ${e.getMessage}")
            System.exit(1)
          case e: Throwable =>
            logger.error(s"fatal unexpected error", e)
            System.exit(1)
        }
      }))

    // todo: explicitly exit on failures - let orchestrator restart pod
    // todo: manage clientIds for QOS on restart
  }

}
