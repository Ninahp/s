addSbtPlugin("com.jsuereth" % "sbt-pgp" % "1.1.1")
credentials += Credentials(
  // System.getenv("AT_RESOLVER_REALM"),
  // System.getenv("AT_RESOLVER_HOST"),
  // System.getenv("AT_RESOLVER_USER"),
  // System.getenv("AT_RESOLVER_PASS")
  "Sonatype Nexus Repository Manager",
  "deino.at-internal.com",
  "keegan",
  "goblins31"
)