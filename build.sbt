name := "scalachikoro"

version := "0.1.0"

scalaVersion := "2.12.8"

val akkaV = "2.5.13"
val akkaRemote = "com.typesafe.akka" %% "akka-remote" % akkaV
val akkaActor = "com.typesafe.akka" %% "akka-actor" % akkaV
val akkaTestKit = "com.typesafe.akka" %% "akka-testkit" % akkaV % Test

val akkaDependencies = Seq(akkaRemote, akkaActor, akkaTestKit)

val scalaTest = "org.scalatest" %% "scalatest" % "2.2.4" % Test

lazy val commons = Project(
  id = "commons",
  base = file("commons"))
  .settings(
    name := "commons"
  )

lazy val server = Project(
  id = "server",
  base = file("server"))
  .enablePlugins(PackPlugin)
  .settings(
    name := "server",
    libraryDependencies ++= akkaDependencies
  ).dependsOn(commons)

lazy val client = Project(
  id = "client",
  base = file("client"))
  .enablePlugins(PackPlugin)
  .settings(
    name := "client",
    libraryDependencies ++= akkaDependencies
  ).dependsOn(commons)