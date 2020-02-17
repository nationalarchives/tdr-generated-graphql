import Dependencies._
import sbt.url
import ReleaseTransformations._

lazy val supportedScalaVersions = List("2.13.0", "2.12.8")
ThisBuild / version := (version in ThisBuild).value
ThisBuild / organization := "uk.gov.nationalarchives"
ThisBuild / organizationName := "National Archives"

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

// Remove all additional repository other than Maven Central from POM
ThisBuild / pomIncludeRepository := { _ => false }
ThisBuild / publishTo := sonatypePublishToBundle.value
ThisBuild / publishMavenStyle := true

useGpgPinentry := true

resolvers +=
  "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

graphqlCodegenStyle := Apollo
graphqlCodegenJson := JsonCodec.Circe
releasePublishArtifactsAction := PgpKeys.publishSigned.value

releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  runTest,
  setReleaseVersion,
  commitReleaseVersion,
  tagRelease,
  releaseStepCommandAndRemaining("+publishSigned"),
  releaseStepCommand("sonatypeBundleRelease"),
  setNextVersion,
  commitNextVersion,
  pushChanges
)

lazy val root = (project in file("."))
  .settings(
    name := "tdr-generated-graphql",
    libraryDependencies ++=
      Seq(
        scalaTest % Test,
        sangria,
        circeCore,
        circeGeneric
      ),
    crossScalaVersions := supportedScalaVersions

  ).enablePlugins(GraphQLCodegenPlugin)