name := "DtlabIngestMqtt"

fork := true
javaOptions in test ++= Seq(
  "-Xms512M",
  "-Xmx2048M",
  "-XX:MaxPermSize=2048M",
  "-XX:+CMSClassUnloadingEnabled"
)

parallelExecution in test := false

version := "1.0"

val akkaHttpVersion = "10.5.3"
scalaVersion := "2.13.13"
val akkaVersion = "2.8.5"

libraryDependencies ++=
  Seq(
    "com.typesafe" % "config" % "1.4.3",
    "com.typesafe.scala-logging" %% "scala-logging" % "3.9.5",
    "org.slf4j" % "jul-to-slf4j" % "2.0.11",
    "ch.qos.logback" % "logback-classic" % "1.4.14",
    "org.slf4j" % "jul-to-slf4j" % "2.0.11",
    "org.typelevel" %% "cats-core" % "2.10.0",
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-stream" % akkaVersion,
    "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
    "org.json4s" %% "json4s-native" % "4.0.7",
    "com.lightbend.akka" %% "akka-stream-alpakka-mqtt" % "6.0.2",
    "org.scalatest" %% "scalatest" % "3.2.18" % "test"
  )

dependencyOverrides ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion
)

mainClass in assembly := Some("somind.dtlab.ingest.mqtt.Main")
assemblyJarName in assembly := "DtlabIngestMqtt.jar"

assemblyMergeStrategy in assembly := {
  case PathList("reference.conf")                      => MergeStrategy.concat
  case x if x.endsWith("io.netty.versions.properties") => MergeStrategy.first
  // case PathList("META-INF", _ @ _*) => MergeStrategy.discard
  case PathList("META-INF", "MANIFEST.MF")  => MergeStrategy.discard
  case PathList("META-INF", "ECLIPSE_.RSA") => MergeStrategy.discard
  case PathList("META-INF", "ECLIPSE_.SF")  => MergeStrategy.discard
  // case x if x.endsWith("MANIFEST.MF") => MergeStrategy.last
  case _ => MergeStrategy.first
}
