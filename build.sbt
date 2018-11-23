val ATSnapshots = "AT Snapshots" at "https://deino.at-internal.com/repository/maven-snapshots/"
val ATReleases  = "AT Releases"  at "https://deino.at-internal.com/repository/maven-releases/"

name := "elmer"

version := "0.1"

scalaVersion := "2.12.6"

resolvers    ++= Seq(
  ATSnapshots,
  ATReleases,
  "Typesafe repository releases" at "http://repo.typesafe.com/typesafe/releases/",
  "Confluent Maven Repository" at "http://packages.confluent.io/maven/"
  
)

libraryDependencies ++= Seq (
  "com.typesafe.akka" %% "akka-actor"               % "2.5.17",
  "com.typesafe.akka" %% "akka-slf4j"               % "2.5.17",
  "com.typesafe.akka" %% "akka-http"                % "10.1.5",
  "com.typesafe.akka" %% "akka-stream"              % "2.5.17",
  "com.typesafe.akka" %% "akka-http-spray-json"     % "10.1.5",
  "io.atlabs"         %% "horus-core"               % "0.1.4",
  "com.typesafe.akka" %% "akka-testkit"             % "2.5.17"  % Test,
  "org.scalatest"     %% "scalatest"                % "3.0.5"   % Test

)
