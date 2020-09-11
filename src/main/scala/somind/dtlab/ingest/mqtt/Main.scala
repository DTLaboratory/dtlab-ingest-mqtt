package somind.dtlab.ingest.mqtt

import akka.Done
import com.typesafe.scalalogging.LazyLogging

import scala.concurrent.Future
import scala.util.{Failure, Success}
import Conf._

import scala.concurrent.ExecutionContext.Implicits.global

object Main extends App with LazyLogging {

  def handleTerminate(result: Future[Done]): Unit = {
    result onComplete {
      case Success(_) =>
        logger.warn("success. but stream should not end!")
        actorSystem.terminate()
      case Failure(e) =>
        logger.error(s"failure. stream should not end! $e", e)
        actorSystem.terminate()
    }
  }

  handleTerminate(Stream())

}
