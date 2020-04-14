ThisBuild / scalaVersion := "2.13.1"

ThisBuild / organization := "com.milton-pagan"

lazy val hello = (project in file("."))
  .settings(
    name := "ScalaMedianFilter",
  )
