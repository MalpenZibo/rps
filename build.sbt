inThisBuild(
  List(
    name := "rps",
    scalaVersion := "2.12.10",
    resolvers += "buildo at bintray" at "https://dl.bintray.com/buildo/maven",
    libraryDependencies ++= Seq(
      "io.buildo" %% "enumero" % "1.2.1",
      "io.buildo" %% "enumero-circe-support" % "1.2.1",
      "io.buildo" %% "wiro-http-server" % "0.8.1",
      "com.typesafe.slick" %% "slick" % "3.3.2",
      "com.h2database" % "h2" % "1.4.187",
      "org.slf4j" % "slf4j-nop" % "1.6.4",
      "com.typesafe.akka" %% "akka-http" % "10.1.11",
      "de.heikoseeberger" %% "akka-http-circe" % "1.18.0",
      "io.circe" %% "circe-core" % "0.8.0",
      "io.circe" %% "circe-generic" % "0.8.0",  
      "org.scalatest" %% "scalatest" % "3.1.1" % "test",
      "com.typesafe.akka" %% "akka-stream-testkit" % "2.6.3",
      "com.typesafe.akka" %% "akka-http-testkit" % "10.1.11"
    ),
    addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full),
    scalacOptions ++= Seq(
      "-Yrangepos",          // required by SemanticDB compiler plugin
      "-Ywarn-unused-import" // required by `RemoveUnused` rule
    ),
  )
)

lazy val myProject = (project in file(".")).
  settings(
    assemblyJarName in assembly := "rps.jar",
  )