package somind.dtlab.ingest.mqtt.utils

import akka.http.scaladsl.model._
import akka.http.scaladsl.{Http, HttpExt}
import com.typesafe.scalalogging.LazyLogging
import somind.dtlab.ingest.mqtt.Conf._

import scala.concurrent.{Await, Future, TimeoutException}

object PostString extends LazyLogging {

  val http: HttpExt = Http(actorSystem)
  val request: HttpRequest = HttpRequest().withMethod(HttpMethods.POST)

  def apply(payload: String): Option[StatusCode] = {
    try {

      val results = dtlabIngestUris.map(uriString => {
        val f = PostString.applyAsync(payload, uriString)
        val r = Await.result(f, webhookTimeoutSeconds)
        if (!r.status.isSuccess()) {
          logger.warn(s"post not successful: $r")
          return None
        }
        logger.debug(s"post successful")
        Some(r.status)
      })
      results.last
    } catch {
      case e: TimeoutException =>
        logger.error(s"remote system is timing out: ${e.getMessage}")
        System.exit(1)
        None
      case e: Throwable =>
        logger.error(s"fatal unexpected error", e)
        System.exit(1)
        None
    }
  }

  def applyAsync(telem: String, uriString: String): Future[HttpResponse] = {

    val newRequest =
      request
        .withEntity(entity = HttpEntity(telemetryContentType, telem))
        .withUri(uri = Uri(uriString))

    logger.debug(s"posting request to: " + newRequest)
    http.singleRequest(newRequest)
  }

}
