name := "Codex"

version := "999-SNAPSHOT"

scalaVersion := "2.11.8"

lazy val codex = (project in file("."))
  .enablePlugins(PlayScala)
  .configs(IntegrationTest)
  .configs(UnitTest)
  .settings(inConfig(IntegrationTest)(Defaults.testTasks) : _*)
  .settings(inConfig(UnitTest)(Defaults.testTasks) : _*)
  .settings(
    testOptions in UnitTest := Seq(Tests.Filter(unitFilter)),
    testOptions in IntegrationTest := Seq(Tests.Filter(itFilter))
  )
  .settings(
    javaOptions in Test +="-Dlogger.resource=logback-test.xml",
    javaOptions in UnitTest +="-Dlogger.resource=logback-test.xml",
    javaOptions in IntegrationTest +="-Dlogger.resource=logback-test.xml"
  )

libraryDependencies ++= Seq(
  appDependencies,
  testDependencies
).flatten

libraryDependencies += filters

lazy val appDependencies = Seq(
  "org.webjars" %% "webjars-play" % "2.5.0",
  "org.webjars" % "bootstrap" % "3.3.6",
  "org.reactivemongo" %% "play2-reactivemongo" % "0.11.14",
  "com.typesafe.play" %% "play-mailer" % "5.0.0"
)

lazy val testDependencies = Seq(
  "org.scalatest" %% "scalatest" % "3.0.0-RC4" % "test"
)

lazy val UnitTest = config("unit") extend(Test)
lazy val IntegrationTest = config("it") extend(Test)

def itFilter(name: String): Boolean = name endsWith "IT"
def unitFilter(name: String): Boolean = name endsWith "Spec"

// test coverage config
coverageExcludedPackages := """controllers\..*Reverse.*;router.Routes.*;views.html.*;"""
coverageMinimum := 100
coverageFailOnMinimum := true
