import Dependencies._
import sbt.url

ThisBuild / version := (ThisBuild / version).value
ThisBuild / organization := "uk.gov.nationalarchives"
ThisBuild / organizationName := "National Archives"

scalaVersion := "2.13.3"

ThisBuild / scmInfo := Some(
  ScmInfo(
    url("https://github.com/nationalarchives/tdr-generated-graphql"),
    "git@github.com:nationalarchives/tdr-generated-graphql.git"
  )
)
ThisBuild / developers := List(
  Developer(
    id    = "SP",
    name  = "Sam Palmer",
    email = "sam.palmer@nationalarchives.gov.uk",
    url   = url("http://tdr-transfer-integration.nationalarchives.gov.uk")
  )
)

ThisBuild / description := "Classes to be used by the graphql client to communicate with the TDR graphql API"
ThisBuild / licenses := List("MIT" -> new URL("https://choosealicense.com/licenses/mit/"))
ThisBuild / homepage := Some(url("https://github.com/nationalarchives/tdr-consignment-api-data"))

ThisBuild / publishTo := Some("GitHub <GITHUB_OWNER> Apache Maven Packages" at "https://maven.pkg.github.com/<GITHUB_OWNER>/<GITHUB_PROJECT>")
ThisBuild / publishMavenStyle := true
ThisBuild / credentials += Credentials(
  "GitHub Package Registry",
  "maven.pkg.github.com",
  "<GITHUB_OWNER>",
  System.getenv("GITHUB_TOKEN")
)

resolvers +=
  "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

graphqlCodegenStyle := Apollo
graphqlCodegenJson := JsonCodec.Circe
graphqlCodegenImports ++= List("java.util.UUID", "java.time.ZonedDateTime", "java.time.LocalDateTime")

lazy val root = (project in file("."))
  .settings(
    name := "tdr-generated-graphql",
    libraryDependencies ++=
      Seq(
        scalaTest % Test,
        sangria,
        circeCore,
        circeGeneric
      )

  ).enablePlugins(GraphQLCodegenPlugin)
