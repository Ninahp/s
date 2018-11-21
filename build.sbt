name := "elmer"

version := "0.1"

scalaVersion := "2.12.7"
lazy val akkaVersion = "2.5.16"

libraryDependencies ++= Seq("io.circe" %% "circe-generic" % "0.10.0",
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-http" % "10.1.5",
  "com.typesafe.akka" %% "akka-stream" % "2.5.12",
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion,
  "com.typesafe.akka" %% "akka-http-testkit" % "10.1.5",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.5",
  "de.heikoseeberger" %% "akka-http-circe" % "1.22.0",
  "org.scalatest" %% "scalatest" % "3.0.5" % "test"
)
