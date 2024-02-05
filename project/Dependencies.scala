import sbt._

object Dependencies {
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.2.17"
  lazy val sangria = "org.sangria-graphql" %% "sangria" % "4.1.0"
  lazy val circeCore = "io.circe" %% "circe-core" % "0.14.6"
  lazy val circeGeneric = "io.circe" %% "circe-generic" % "0.14.6"
}
