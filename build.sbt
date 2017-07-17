import sbt.Path._
import Dependencies._

lazy val commonSettings = Seq(
  version := "1.0.0",
  scalaVersion := "2.11.8",
  scalacOptions ++= Seq("-feature"),
  logLevel := Level.Info,
  resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
)

lazy val root = (project in file(".")).
settings(commonSettings: _*).
settings(
  name := "Json database search engine",
  libraryDependencies ++= commonDeps,
    
  mainClass in assembly := Some("SearchEngine"),
  assemblyJarName in assembly := "searchEngine.jar"
)
