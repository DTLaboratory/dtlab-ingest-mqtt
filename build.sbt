name := "DtlabIngestMqtt"

fork := true
javaOptions in test ++= Seq(
  "-Xms512M", "-Xmx2048M",
  "-XX:MaxPermSize=2048M",
  "-XX:+CMSClassUnloadingEnabled"
)

parallelExecution in test := false

version := "1.0"

val akkaHttpVersion = "10.2.4"
scalaVersion := "2.13.5"
val akkaVersion = "2.6.14"

libraryDependencies ++=
  Seq(
    "com.typesafe" % "config" % "1.3.4",
    "com.typesafe.scala-logging" %% "scala-logging" % "3.9.3",
    "ch.qos.logback" % "logback-classic" % "1.2.3",
    "org.slf4j" % "jul-to-slf4j" % "1.7.30",

    "org.typelevel" %% "cats-core" % "2.6.0",
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-stream" % akkaVersion,
    "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
    "org.json4s" %% "json4s-native" % "3.6.11",

    "com.lightbend.akka" %% "akka-stream-alpakka-mqtt" % "2.0.2",

    "org.scalatest" %% "scalatest" % "3.2.8" % "test"
  )

dependencyOverrides ++= Seq(
  "com.typesafe.akka" %% "akka-actor"  % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion
)

mainClass in assembly := Some("somind.dtlab.ingest.mqtt.Main")
assemblyJarName in assembly := "DtlabIngestMqtt.jar"

assemblyMergeStrategy in assembly := {
  case PathList("reference.conf") => MergeStrategy.concat
  case x if x.endsWith("io.netty.versions.properties") => MergeStrategy.first
  //case PathList("META-INF", _ @ _*) => MergeStrategy.discard
  case PathList("META-INF", "MANIFEST.MF") => MergeStrategy.discard
  case PathList("META-INF", "ECLIPSE_.RSA") => MergeStrategy.discard
  case PathList("META-INF", "ECLIPSE_.SF") => MergeStrategy.discard
  //case x if x.endsWith("MANIFEST.MF") => MergeStrategy.last
  case _ => MergeStrategy.first
}

