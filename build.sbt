val ATSnapshots = "AT Snapshots" at "https://deino.at-internal.com/repository/maven-snapshots/"
val ATReleases  = "AT Releases"  at "https://deino.at-internal.com/repository/maven-releases/"

lazy val sharedSettings = Seq(
  organization := "com.africasTalking",
  version      := "0.1.0",
  scalaVersion := "2.12.6",
  resolvers    ++= Seq(
    ATSnapshots,
    ATReleases,
    "Typesafe repository releases" at "http://repo.typesafe.com/typesafe/releases/",
    "Confluent Maven Repository" at "http://packages.confluent.io/maven/"
  ),
  scalacOptions ++= Seq(
    "-deprecation",
    "-feature",
    "-unchecked"
  )
)

val akkaVersion      = "2.5.16"
val akkaHttpVersion  = "10.1.5"
val scalaTestVersion = "3.0.5"
lazy val elmer = (project in file("."))
  .aggregate(core, food, web)

lazy val core = (project in file("core")).
  settings(
    sharedSettings,
    libraryDependencies ++= Seq(
      "com.typesafe.akka"             %% "akka-actor"           % akkaVersion,
      "com.typesafe.akka"             %% "akka-stream"          % akkaVersion,
      "com.typesafe.akka"             %% "akka-http-spray-json" % akkaHttpVersion,
      "com.africasTalking"            %% "atlas-core"           % "0.1.0",
      "com.africasTalking"            %% "atlas-crunch"         % "0.1.0",
      "io.atlabs"                     %% "horus-core"           % "0.1.0",
      "com.github.nscala-time"        %% "nscala-time"          % "2.20.0",
      "commons-daemon"                %  "commons-daemon"       % "1.1.0",
      "org.scalatest"                 %% "scalatest"            % scalaTestVersion % Test,
      "com.typesafe.akka"             %% "akka-testkit"         % akkaVersion      % Test,
      "org.lz4"                       %  "lz4-java"             % "1.4.1"          % Test
    
    )
  )

lazy val food = (project in file("food")).
  settings(
    sharedSettings,
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-testkit" % akkaVersion      % Test,
      "org.scalatest"     %% "scalatest"    % scalaTestVersion % Test
    )
  ).dependsOn(core)

lazy val web = (project in file("web")).
  settings(
    sharedSettings,
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-testkit"      % akkaVersion      % Test,
      "org.scalatest"     %% "scalatest"         % scalaTestVersion % Test,
      "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion  % Test
    )
  ).dependsOn(core, food)

  cancelable in Global := true
