import sbt._

object Dependencies { 


  // all the libraries used for the project
  //
  val commonDeps = Seq(
    "org.scalatest" %% "scalatest" % "3.0.1",
    "com.typesafe" % "config" % "1.0.2",
    "com.fasterxml.jackson.core" % "jackson-databind" % "2.8.9",
    "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.8.9"
  )

}
