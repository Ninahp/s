val ATSnapshots = "AT Snapshots" at "https://deino.at-internal.com/repository/maven-snapshots/"
val ATReleases  = "AT Releases"  at "https://deino.at-internal.com/repository/maven-releases/"

enablePlugins(JavaAppPackaging,JavaServerAppPackaging,sbtdocker.DockerPlugin,DockerComposePlugin)

lazy val akkaVersion = "2.5.16"
lazy val akkaStreamVersion = "2.5.16"
lazy val akkaHttpVersion = "10.1.5"

lazy val sharedSettings = Seq(
  organization := "com.africasTalking",
  version      := "0.1.0",
  scalaVersion := "2.12.6",
  resolvers ++= Seq(
    "rediscala" at "http://dl.bintray.com/etaty/maven",
    "Typesafe repository releases" at "http://repo.typesafe.com/typesafe/releases/"
  ),
  scalacOptions ++= Seq(
    "-deprecation",
    "-feature",
    "-unchecked"
  ),
  libraryDependencies ++= Seq(
    "com.typesafe.akka" %% "akka-actor"           % akkaVersion,
    "com.typesafe.akka" %% "akka-testkit"         % akkaVersion        % Test,
    "com.typesafe.akka" %% "akka-stream"          % akkaStreamVersion,
    "com.typesafe.akka" %% "akka-stream-testkit"  % akkaStreamVersion  % Test,
    "com.typesafe.akka" %% "akka-http"            % akkaHttpVersion,
    "io.spray"          %%  "spray-json"          % "1.3.4",
    "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
    "org.scalatest"     %% "scalatest"            % "3.0.5"            % "test",
    "org.scalactic"     %% "scalactic"            % "3.0.5",
    "com.typesafe.akka" %% "akka-slf4j"           % akkaVersion,
    "ch.qos.logback"    % "logback-classic"       % "1.2.3"
  )
)

lazy val elmer  = (project in file(".")).aggregate(core, web, worker)

lazy val core   = (project in file("core")).settings(sharedSettings,
  libraryDependencies ++= Seq(
    "io.atlabs"         %% "horus-core"           % "0.1.0"
))
lazy val web    = (project in file("web")).dependsOn(worker).settings(sharedSettings,
  libraryDependencies ++= Seq(
    "com.typesafe.akka" %% "akka-http-testkit"    % akkaHttpVersion    % Test,
  )
)
lazy val worker = (project in file("worker")).dependsOn(core).settings(sharedSettings,
  libraryDependencies ++= Seq(
    "com.typesafe.akka" %% "akka-http-testkit"    % akkaHttpVersion    % Test,
  )
)