import Dependencies._
import sbt.url
import ReleaseTransformations._
import java.io.FileWriter

ThisBuild / organization := "uk.gov.nationalarchives"
ThisBuild / organizationName := "National Archives"

scalaVersion := "2.13.10"

lazy val setLatestTagOutput = taskKey[Unit]("Sets a GitHub actions output for the latest tag")

setLatestTagOutput := {
  val fileWriter = new FileWriter(sys.env("GITHUB_OUTPUT"), true)
  fileWriter.write(s"latest-tag=${(ThisBuild / version).value}\n")
  fileWriter.close()
}

scmInfo := Some(
  ScmInfo(
    url("https://github.com/nationalarchives/tdr-generated-graphql"),
    "git@github.com:nationalarchives/tdr-generated-graphql.git"
  )
)

developers := List(
  Developer(
    id    = "tna-digital-archiving-jenkins",
    name  = "TNA Digital Archiving",
    email = "digitalpreservation@nationalarchives.gov.uk",
    url   = url("https://github.com/nationalarchives/tdr-generated-grapqhl")
  )
)

description := "Classes to be used by the graphql client to communicate with the TDR graphql API"
licenses := List("MIT" -> new URL("https://choosealicense.com/licenses/mit/"))
homepage := Some(url("https://github.com/nationalarchives/tdr-generated-grapqhl"))

useGpgPinentry := true
publishTo := sonatypePublishToBundle.value
publishMavenStyle := true

releaseProcess := Seq[ReleaseStep](
  checkSnapshotDependencies,
  inquireVersions,
  runClean,
  runTest,
  setReleaseVersion,
  releaseStepTask(setLatestTagOutput),
  commitReleaseVersion,
  tagRelease,
  releaseStepCommand("publishSigned"),
  releaseStepCommand("sonatypeBundleRelease"),
  setNextVersion,
  commitNextVersion,
  pushChanges
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
