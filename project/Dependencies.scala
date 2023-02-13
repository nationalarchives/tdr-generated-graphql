import sbt._

object Dependencies {
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.2.15"
  lazy val sangria = "org.sangria-graphql" %% "sangria" % "3.5.2"
  lazy val circeCore = "io.circe" %% "circe-core" % "0.14.4"
  lazy val circeGeneric = "io.circe" %% "circe-generic" % "0.14.4"
}
