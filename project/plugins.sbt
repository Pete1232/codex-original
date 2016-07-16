logLevel := Level.Warn

// The Typesafe repository
resolvers += "Typesafe repository" at "https://repo.typesafe.com/typesafe/maven-releases/"

// Use the Play sbt plugin for Play projects
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.5.4")
// Produce test coverage reports
addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.3.5")
