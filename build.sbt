lazy val sharedSettings = Seq(
  organization := "com.africasTalking",
  version      := "0.1.0",
  scalaVersion := "2.12.6",
  resolvers    ++= Seq(
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
  .aggregate(core, productservice, web)

lazy val core = (project in file("core")).
  settings(
    sharedSettings,
    libraryDependencies ++= Seq(
      "com.typesafe.akka"             %% "akka-actor"           % akkaVersion,
      "com.typesafe.akka"             %% "akka-stream"          % akkaVersion,
      "com.typesafe.akka"             %% "akka-http-spray-json" % akkaHttpVersion,
      "com.github.nscala-time"        %% "nscala-time"          % "2.20.0",
      "commons-daemon"                %  "commons-daemon"       % "1.1.0",
      "org.scalatest"                 %% "scalatest"            % scalaTestVersion % Test,
      "com.typesafe.akka"             %% "akka-testkit"         % akkaVersion      % Test,
      "org.lz4"                       %  "lz4-java"             % "1.4.1"          % Test,
      "org.json4s" %% "json4s-core" % "3.5.0",
      "org.json4s" %% "json4s-jackson" % "3.5.0",
      "de.heikoseeberger" %% "akka-http-json4s" % "1.11.0"
    
    )
  )

lazy val productservice = (project in file("productservice")).
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
  ).dependsOn(core, productservice)

  cancelable in Global := true
