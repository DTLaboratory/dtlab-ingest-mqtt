name := "DtLabIngestMqtt"

fork := true
javaOptions in test ++= Seq(
  "-Xms128M", "-Xmx256M",
  "-XX:MaxPermSize=256M",
  "-XX:+CMSClassUnloadingEnabled"
)

parallelExecution in test := false

version := "1.0"

scalaVersion := "2.12.6"
val akkaVersion = "2.5.31"

libraryDependencies ++=
  Seq(

    "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",

    "com.typesafe" % "config" % "1.4.0",
    "ch.qos.logback" % "logback-classic" % "1.1.11",

    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-stream" % akkaVersion,

    "com.lightbend.akka" %% "akka-stream-alpakka-mqtt" % "0.20",

    "tech.navicore" %% "navipath" % "4.0.2",

    "org.scalatest" %% "scalatest" % "3.0.9" % "test"

  )

mainClass in assembly := Some("somind.dtlab.ingest.mqtt.Main")
assemblyJarName in assembly := "DtLabIngestMqtt.jar"

assemblyMergeStrategy in assembly := {
  case PathList("reference.conf") => MergeStrategy.concat
  case x if x.endsWith("io.netty.versions.properties") => MergeStrategy.first
  case PathList("META-INF", _ @ _*) => MergeStrategy.discard
  case _ => MergeStrategy.first
}
