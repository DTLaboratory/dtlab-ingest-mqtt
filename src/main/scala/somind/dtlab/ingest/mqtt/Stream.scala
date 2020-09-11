package somind.dtlab.ingest.mqtt

import akka.Done
import akka.stream.alpakka.mqtt._
import akka.stream.alpakka.mqtt.scaladsl._
import akka.stream.scaladsl.{Sink, Source}
import com.typesafe.scalalogging.LazyLogging
import javax.net.ssl.SSLContext
import navicore.data.navipath.dsl.NaviPathSyntax._
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import somind.dtlab.ingest.mqtt.Conf._

import scala.concurrent.Future
import scala.concurrent.duration._
import scala.language.implicitConversions

object Stream extends LazyLogging {

  def getKey(text: String): String = {
    text
      .query[String]("$.DevAddr")
      .getOrElse(text.hashCode().toString)
  }

  val connectionSettings: MqttConnectionSettings =
    MqttConnectionSettings(
      mqttUrl,
      mqttClientId,
      new MemoryPersistence
    ).withAuth(mqttUser, mqttPwd)
      .withAutomaticReconnect(true)
      .withKeepAliveInterval(15 seconds)
      .withSocketFactory(SSLContext.getDefault.getSocketFactory)

  def apply(): Future[Done] = {

    val mqttSource: Source[MqttMessageWithAck, Future[Done]] =
      MqttSource.atLeastOnce(
        connectionSettings
          .withClientId(clientId = mqttClientId)
          .withCleanSession(false),
        MqttSubscriptions(mqttTopic, MqttQoS.AtLeastOnce),
        bufferSize = 8
      )

    mqttSource
      .runWith(Sink.foreach(msg => {

        // todo: write to dtlab ingest in a flow using via
        // todo: write to dtlab ingest in a flow using via
        // todo: write to dtlab ingest in a flow using via
        msg.ack()

        println(msg.message.payload.toString())

      }))

  }

}
