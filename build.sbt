name := "DtlabIngestMqtt"

fork := true
javaOptions in test ++= Seq(
  "-Xms512M", "-Xmx2048M",
  "-XX:MaxPermSize=2048M",
  "-XX:+CMSClassUnloadingEnabled"
)

parallelExecution in test := false

scalacOptions += "-Ypartial-unification"

version := "1.0"

scalaVersion := "2.12.8"
val akkaVersion = "2.5.32"

libraryDependencies ++=
  Seq(
    "com.typesafe" % "config" % "1.3.3",
    "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
    "ch.qos.logback" % "logback-classic" % "1.2.3",
    "org.slf4j" % "jul-to-slf4j" % "1.7.30",

    "org.typelevel" %% "cats-core" % "1.6.0",
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.typesafe.akka" %% "akka-stream" % akkaVersion,

    "com.lightbend.akka" %% "akka-stream-alpakka-mqtt" % "2.0.2",

    "org.scalatest" %% "scalatest" % "3.0.5" % "test"
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

