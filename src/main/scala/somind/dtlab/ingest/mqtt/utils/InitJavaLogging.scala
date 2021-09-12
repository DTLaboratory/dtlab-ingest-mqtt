package somind.dtlab.ingest.mqtt.utils

import java.util.logging.Level

import com.typesafe.scalalogging.LazyLogging
import org.slf4j.LoggerFactory

import scala.sys.env

/** If you have a java lib dep like eclipse.paho that you want to see logging
  * from, it sucks to be you.
  *
  * You must init the java.util.logging subsystem with jul-to-slf4j to get
  * Logback and LazyLogging to all play together :(
  *
  * dependent on build.sbt libs "ch.qos.logback" % "logback-classic" % "?.?.?",
  * "org.slf4j" % "jul-to-slf4j" % "?.?.?",
  */
object InitJavaLogging extends LazyLogging {

  def apply(): Unit = {
    import java.util.logging.LogManager

    import org.slf4j.bridge.SLF4JBridgeHandler

    val pin: List[java.util.logging.Logger] = List(
      java.util.logging.Logger.getLogger(""),
      java.util.logging.Logger.getLogger("org.eclipse.paho.client.mqttv"),
      java.util.logging.Logger.getLogger("org.eclipse.paho")
    )

    def canInstallBridgeHandler = {
      try {
        Class.forName(
          "org.slf4j.impl.JDK14LoggerFactory",
          false,
          this.getClass.getClassLoader
        )
        LoggerFactory
          .getLogger(this.getClass)
          .warn(
            "Detected [org.slf4j.impl.JDK14LoggerFactory] on classpath. " +
              "SLF4JBridgeHandler cannot be installed, see: http://www.slf4j.org/legacy.html#julRecursion"
          )
        false
      } catch {
        case _: ClassNotFoundException =>
          true
      }
    }

    val sysLogLevel = env.getOrElse("SYS_LOG_LEVEL", "info") match {
      case "debug" => Level.FINER
      case "trace" => Level.FINEST
      case "error" => Level.WARNING
      case _       => Level.INFO
    }

    if (canInstallBridgeHandler) {
      LogManager.getLogManager.reset()
      SLF4JBridgeHandler.removeHandlersForRootLogger()
      SLF4JBridgeHandler.install()

      java.util.logging.Logger
        .getLogger("")
        .addHandler(new SLF4JBridgeHandler())
      pin.foreach(l => l.setLevel(sysLogLevel))

      logger.info("org.slf4j.bridge.SLF4JBridgeHandler installed.")
    }

  }

}
