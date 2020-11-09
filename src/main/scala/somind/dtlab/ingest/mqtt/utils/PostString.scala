package somind.dtlab.ingest.mqtt.utils

import akka.http.scaladsl.model._
import akka.http.scaladsl.{Http, HttpExt}
import com.typesafe.scalalogging.LazyLogging
import somind.dtlab.ingest.mqtt.Conf._

import scala.concurrent.Future

object PostString extends LazyLogging {

  val http: HttpExt = Http(actorSystem)
  val request: HttpRequest = HttpRequest()
    .withUri(uri = Uri(dtlabIngestUri))

  def apply(telem: String): Future[HttpResponse] = {

    val newRequest =
      request.withEntity(entity = HttpEntity(telemetryContentType, telem))

    logger.debug(s"sending request to: " + newRequest)
    http.singleRequest(newRequest)
  }

}
