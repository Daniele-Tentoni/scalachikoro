name := "scalachikoro"

version := "0.1.0"

scalaVersion := "2.12.8"

val akkaV = "2.5.13"
val akkaRemote = "com.typesafe.akka" %% "akka-remote" % akkaV
val akkaActor = "com.typesafe.akka" %% "akka-actor" % akkaV
val akkaTestKit = "com.typesafe.akka" %% "akka-testkit" % akkaV % Test

val akkaDependencies = Seq(akkaRemote, akkaActor, akkaTestKit)

val scalaTest = "org.scalatest" %% "scalatest" % "2.2.4" % Test

// Determine OS version of JavaFX binaries
lazy val osName = System.getProperty("os.name") match {
  case n if n.startsWith("Linux") => "linux"
  case n if n.startsWith("Mac") => "mac"
  case n if n.startsWith("Windows") => "win"
  case _ => throw new Exception("Unknown platform!")
}

val scalafx = "org.scalafx" %% "scalafx" % "14-R19"

// Add JavaFX dependencies
lazy val javaFXModules = Seq("base", "controls", "fxml", "graphics", "media", "swing", "web")
lazy val scalaFXDep = javaFXModules.map(m =>
  "org.openjfx" % s"javafx-$m" % "14.0.1" classifier osName
)

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
    libraryDependencies ++= (akkaDependencies ++ (scalaFXDep :+ scalafx))
  ).dependsOn(commons)