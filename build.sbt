ThisBuild / scalaVersion := "2.12.11"

ThisBuild / organization := "com.milton-pagan"

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.6.4"

lazy val ScalaMedianFilter = (project in file("."))
  .settings(
    name := "ScalaMedianFilter",
  )
