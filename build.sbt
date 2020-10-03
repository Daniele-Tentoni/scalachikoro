name := "scalachikoro"

version := "0.1.0"

scalaVersion := "2.10.3"

val akkaV = "2.5.13"
val akkaTyped = "com.typesafe.akka" %% "akka-actor-typed" % akkaV
val akkaRemote = "com.typesafe.akka" %% "akka-remote" % akkaV
val akkaActor = "com.typesafe.akka" %% "akka-actor" % akkaV
val akkaTestKit = "com.typesafe.akka" %% "akka-testkit" % akkaV % Test

val akkaDependencies = Seq(akkaTyped, akkaRemote, akkaActor, akkaTestKit)

val scalactic = "org.scalactic" %% "scalactic" % "3.2.0"
val scalaTest = "org.scalatest" %% "scalatest" % "3.2.0" % Test
val scalamock = "org.scalamock" %% "scalamock" % "4.4.0" % Test
val scalacheck = "org.scalacheck" %% "scalacheck" % "1.14.1" % Test

val testDependencies = Seq(scalactic, scalaTest, scalamock, scalacheck)

// val junit = "junit" % "junit" % "4.12"
// val cukeCore = "io.cucumber" %% "cucumber-core" % "2.0.1" % Test
// val cuke_run = "com.waioeka.sbt" %% "cucumber-runner" % "0.0.8"
// val cuke_scala = "info.cukes" %% "cucumber-scala" % "1.2.4" % Test
// val cuke_junit = "info.cukes" %% "cucumber-junit" % "1.2.4" % Test
// val cukeJUnit = "io.cucumber" %% "cucumber-junit" % "2.0.1" % Test

// val cukeDependencies = Seq(cuke_run, cuke_scala, cuke_junit)

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

/*
 * Scoverage configurations.
 */
coverageMinimum := 25
coverageFailOnMinimum := true

/*
 * Project configurations.
 */
lazy val commons = Project(
  id = "commons",
  base = file("commons"))
  .settings(
    name := "commons",
    libraryDependencies ++= (testDependencies :+ akkaTyped)
  )

lazy val server = Project(
  id = "server",
  base = file("server"))
  .enablePlugins(PackPlugin)
  .settings(
    name := "server",
    libraryDependencies ++= (akkaDependencies ++ testDependencies)
  ).dependsOn(commons)

lazy val client = Project(
  id = "client",
  base = file("client"))
  .enablePlugins(PackPlugin)
  .settings(
    name := "client",
    libraryDependencies ++= (akkaDependencies ++ (scalaFXDep :+ scalafx) ++ testDependencies)
  ).dependsOn(commons)