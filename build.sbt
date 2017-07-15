import sbt.Path._
import Dependencies._

lazy val commonSettings = Seq(
  version := "1.0.0",
  scalaVersion := "2.11.8",
  scalacOptions ++= Seq("-feature"),
  logLevel := Level.Info,
  resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
  resolvers += "mapr-releases" at "http://repository.mapr.com/maven/",
  resolvers += "emueller-bintray" at "http://dl.bintray.com/emueller/maven"
)

lazy val root = (project in file(".")).
settings(commonSettings: _*).
settings(
  name := "Json database search engine",
  libraryDependencies ++= commonDeps,
    
  mainClass in assembly := Some("searchEngine"),
  assemblyJarName in assembly := "searchEngine.jar"
)
