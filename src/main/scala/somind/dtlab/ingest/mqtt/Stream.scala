package somind.dtlab.ingest.mqtt

import akka.Done
import akka.stream.alpakka.mqtt._
import akka.stream.alpakka.mqtt.scaladsl.{MqttCommittableMessage, MqttSource}
import akka.stream.scaladsl.{Sink, Source}
import com.typesafe.scalalogging.LazyLogging
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence
import somind.dtlab.ingest.mqtt.Conf._

import scala.collection.immutable.Map
import scala.concurrent.Future
import scala.concurrent.duration._
import scala.language.implicitConversions
import navicore.data.navipath.dsl.NaviPathSyntax._

object Stream extends LazyLogging {

  def getKey(text: String): String = {
    text
      .query[String]("$.DevAddr")
      .getOrElse(text.hashCode().toString)
  }

  val mqttConsumerettings: MqttSourceSettings = MqttSourceSettings(
    MqttConnectionSettings(
      mqttUrl,
      mqttClientId,
      new MemoryPersistence
    ).withAuth(mqttUser, mqttPwd)
      .withAutomaticReconnect(true)
      .withKeepAliveInterval(15, SECONDS),
    Map(mqttTopic -> MqttQoS.AtLeastOnce)
  )

  // todo: make sure this runs forever ejs
  // todo: make sure this runs forever ejs
  // todo: make sure this runs forever ejs
  // todo: make sure this runs forever ejs
  def apply(): Unit = {

    val mqttSource: Source[MqttCommittableMessage, Future[Done]] =
      MqttSource.atLeastOnce(mqttConsumerettings, bufferSize = 8)

    mqttSource
      .runWith(Sink.foreach(msg => {

        // todo: write to dtlab ingest
        msg.messageArrivedComplete()

      }))

  }

}
