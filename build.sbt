name := "HomePage"

version := "0.1.0"

scalaVersion := "2.11.8"

lazy val home = (project in file(".")).enablePlugins(PlayScala)

libraryDependencies ++= Seq(
  "org.webjars" %% "webjars-play" % "2.5.0",
  "org.webjars" % "bootstrap" % "3.3.6"
)
