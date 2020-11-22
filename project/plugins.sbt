// Use this package to pack the application before release.
addSbtPlugin("org.xerial.sbt" % "sbt-pack" % "0.12")
// Produce Coverage Test Reports in HTML for browsers.
addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.6.1")
// Check for styles issues.
addSbtPlugin("ch.epfl.scala" % "sbt-scalafix" % "0.9.23")