package somind.dtlab.ingest.mqtt.utils

import akka.http.scaladsl.model._
import akka.http.scaladsl.{Http, HttpExt}
import com.typesafe.scalalogging.LazyLogging
import somind.dtlab.ingest.mqtt.Conf._

import scala.concurrent.{Await, Future, TimeoutException}

object PostString extends LazyLogging {

  val http: HttpExt = Http(actorSystem)
  val request: HttpRequest = HttpRequest()
    .withUri(uri = Uri(dtlabIngestUri))

  def apply(payload: String): Option[StatusCode] = {
    try {
      val f = PostString.applyAsync(payload)
      val r = Await.result(f, webhookTimeoutSeconds)
      if (!r.status.isSuccess()) {
        logger.warn(s"post not successful: ${r._3}")
      }
      Some(r.status)
    } catch {
      case e: TimeoutException =>
        logger.error(
          s"remote system is timing out: ${e.getMessage}")
        System.exit(1)
        None
      case e: Throwable =>
        logger.error(s"fatal unexpected error", e)
        System.exit(1)
        None
    }
  }

  def applyAsync(telem: String): Future[HttpResponse] = {

    val newRequest =
      request.withEntity(entity = HttpEntity(telemetryContentType, telem))

    logger.debug(s"sending request to: " + newRequest)
    http.singleRequest(newRequest)
  }

}
