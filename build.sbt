val ATSnapshots = "AT Snapshots" at "https://deino.at-internal.com/repository/maven-snapshots/"
val ATReleases  = "AT Releases"  at "https://deino.at-internal.com/repository/maven-releases/"

lazy val sharedSettings = Seq(
  scalaVersion := "2.12.6",
  version := "0.1.6",
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
  ),
  updateOptions := updateOptions.value.withLatestSnapshots(false),
  test in assembly := {},
  assemblyMergeStrategy in assembly := {
    case "META-INF/io.netty.versions.properties" => MergeStrategy.first
    case PathList("io", "netty", xs @ _*)        => MergeStrategy.last
    case "logback.xml"                           => MergeStrategy.last
    case x                                       =>
      val oldStrategy = (assemblyMergeStrategy in assembly).value
      oldStrategy(x)
  }
)


val akkaVersion      = "2.5.17"
val akkaHttpVersion  = "10.1.5"
val scalaTestVersion = "3.0.5"



lazy val elmer = (project in file("."))
  .aggregate(core, web)

lazy val sharedDependencies = Seq(
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion      % Test,
  "org.scalatest"     %% "scalatest"    % scalaTestVersion % Test
)



lazy val core = (project in file("core")).
  settings(
    sharedSettings,
    libraryDependencies ++= sharedDependencies,
    libraryDependencies ++= Seq(
      "com.typesafe.akka"      %% "akka-actor"           % akkaVersion,
      "com.typesafe.akka"      %% "akka-slf4j"           % akkaVersion,
      "com.typesafe.akka"      %% "akka-http"            % akkaHttpVersion,
      "com.typesafe.akka"      %% "akka-http-spray-json" % akkaHttpVersion,
      "ch.qos.logback"         %  "logback-classic"      % "1.2.1",
      "ch.qos.logback"         %  "logback-core"         % "1.2.1",
      "io.atlabs"              %% "horus-core"           % "0.1.7" 
    )
  )


lazy val worker = (project in file("worker")).
  settings(sharedSettings,
    libraryDependencies ++= sharedDependencies
  ).dependsOn(core)

lazy val web = (project in file("web")).
  settings(
    sharedSettings,
    libraryDependencies ++= sharedDependencies
  ).dependsOn(core,worker)



