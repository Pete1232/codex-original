name := "Codex"

version := "0.1.0"

scalaVersion := "2.11.8"

lazy val codex = (project in file(".")).enablePlugins(PlayScala)

libraryDependencies ++= Seq(
  appDependencies,
  testDependencies
).flatten

lazy val appDependencies = Seq(
  "org.webjars" %% "webjars-play" % "2.5.0",
  "org.webjars" % "bootstrap" % "3.3.6"
)

lazy val testDependencies = Seq(
  "org.scalatest" %% "scalatest" % "3.0.0-RC4" % "test"
)
