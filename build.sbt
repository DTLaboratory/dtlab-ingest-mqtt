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

val akkaHttpVersion = "10.5.0"
scalaVersion := "2.13.10"
val akkaVersion = "2.7.0"

libraryDependencies ++=
  Seq(
    "com.typesafe" % "config" % "1.4.2",
    "com.typesafe.scala-logging" %% "scala-logging" % "3.9.5",
    "org.slf4j" % "jul-to-slf4j" % "2.0.6",
    "ch.qos.logback" % "logback-classic" % "1.4.6",
    "org.slf4j" % "jul-to-slf4j" % "2.0.6",
    "org.typelevel" %% "cats-core" % "2.9.0",
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-stream" % akkaVersion,
    "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
    "org.json4s" %% "json4s-native" % "4.0.6",
    "com.lightbend.akka" %% "akka-stream-alpakka-mqtt" % "5.0.0",
    "org.scalatest" %% "scalatest" % "3.2.15" % "test"
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
