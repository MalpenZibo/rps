ThisBuild / scalaVersion     := "2.12.10"

lazy val root = (project in file("."))
  .settings(
    name := "rps",
    resolvers += "buildo at bintray" at "https://dl.bintray.com/buildo/maven",
    libraryDependencies += "io.buildo" %% "enumero" % "1.4.0",
    addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)
  )
