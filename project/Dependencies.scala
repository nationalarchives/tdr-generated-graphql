import sbt._

object Dependencies {
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.2.19"
  lazy val sangria = "org.sangria-graphql" %% "sangria" % "4.1.1"
  lazy val circeCore = "io.circe" %% "circe-core" % "0.14.9"
  lazy val circeGeneric = "io.circe" %% "circe-generic" % "0.14.9"
}
