val ATSnapshots = "AT Snapshots" at "https://deino.at-internal.com/repository/maven-snapshots/"
val ATReleases  = "AT Releases"  at "https://deino.at-internal.com/repository/maven-releases/"


val akkaVersion        ="2.5.18"
val akkaHttpVersion    ="10.1.5"
val scalaTestVersion   ="3.0.5"
val organization       = "com.elmer"


lazy val sharedDependencies = Seq(
  "com.typesafe.akka"         %% "akka-http-testkit"      % "10.1.5" % Test,
  "com.typesafe.akka"         %% "akka-testkit"           % "2.5.18" % Test
)



lazy val core = (project in file("core")).
  settings(
    sharedSettings,
    libraryDependencies ++= sharedDependencies,
    libraryDependencies Seq(
      "com.typesafe.akka"         %% "akka-actor"             % akkaVersion,
      "com.typesafe.akka"         %% "akka-http"              % akkaHttpVersion,
      "com.typesafe.akka"         %% "akka-slf4j"             % akkaVersion,
      "com.typesafe.akka"         %% "akka-http-spray-json"   % "10.1.5",
      "com.typesafe.slick"        %% "slick"                  % "1.0.1",
      "ch.qos.logback"            %  "logback-core"           % "1.2.1",
      "ch.qos.logback"            % "logback-classic"         % "1.2.3",
      "io.spray"                  %% "spray-json"             % "1.3.3",
      "io.spray"                  % "spray-routing"           % "1.1-M8",
    //"mysql"                     % "mysql-connector-java"    % "5.1.25",
      "net.liftweb"               %% "lift-json"              % "2.5.1",
      "io.atlabs"                 %% "horus-core"             % "0.1.6"

)

resolvers ++= Seq(
  ATSnapshots,
  ATReleases,
  "rediscala" at "http://dl.bintray.com/etaty/maven",
  "Typesafe repository releases" at "http://repo.typesafe.com/typesafe/releases/"
)
