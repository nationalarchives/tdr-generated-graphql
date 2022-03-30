import sbt._

object Dependencies {
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.9"
  lazy val sangria = "org.sangria-graphql" %% "sangria" % "2.0.1"
  lazy val circeCore = "io.circe" %% "circe-core" % "0.14.1"
  lazy val circeGeneric = "io.circe" %% "circe-generic" % "0.14.1"
}
