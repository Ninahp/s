addSbtPlugin("com.eed3si9n"       % "sbt-assembly"        % "0.14.6")
addSbtPlugin("com.jsuereth"       % "sbt-pgp"             % "1.1.1")
addSbtPlugin("com.typesafe.sbt"   % "sbt-native-packager" % "1.3.4")
addSbtPlugin("se.marcuslonnberg"  % "sbt-docker"          % "1.4.1")
addSbtPlugin("com.tapad"          % "sbt-docker-compose"  % "1.0.34")

credentials += Credentials(
  System.getenv("AT_RESOLVER_REALM"),
  System.getenv("AT_RESOLVER_HOST"),
  System.getenv("AT_RESOLVER_USER"),
  System.getenv("AT_RESOLVER_PASS")
)