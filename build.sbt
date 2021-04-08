import ReleaseTransformations._

lazy val V = _root_.scalafix.sbt.BuildInfo

lazy val commonSettings = Def.settings(
  List(
    organization := "com.github.xuwei-k",
    homepage := Some(url("https://github.com/xuwei-k/play-ws-scalafix")),
    licenses := Seq("MIT License" -> url("https://opensource.org/licenses/mit-license")),
    description := "scalafix rule for replace old-play-ws to play-ws-standalone",
    scalaVersion := V.scala212,
    crossScalaVersions := Seq(V.scala212, V.scala213),
    addCompilerPlugin(scalafixSemanticdb),
    releaseCrossBuild := true,
    releaseProcess := Seq[ReleaseStep](
      checkSnapshotDependencies,
      inquireVersions,
      runClean,
      runTest,
      setReleaseVersion,
      commitReleaseVersion,
      tagRelease,
      releaseStepCommandAndRemaining("+publishSigned"),
      releaseStepCommandAndRemaining("sonatypeBundleRelease"),
      setNextVersion,
      commitNextVersion,
      pushChanges
    ),
    pomExtra := (
      <developers>
        <developer>
          <id>xuwei-k</id>
          <name>Kenji Yoshida</name>
          <url>https://github.com/xuwei-k</url>
        </developer>
      </developers>
      <scm>
        <url>git@github.com:xuwei-k/play-ws-scalafix.git</url>
        <connection>scm:git:git@github.com:xuwei-k/play-ws-scalafix.git</connection>
      </scm>
    ),
    publishTo := sonatypePublishToBundle.value,
    (Compile / doc / scalacOptions) ++= {
      val hash = sys.process.Process("git rev-parse HEAD").lineStream_!.head
      Seq(
        "-sourcepath",
        (LocalRootProject / baseDirectory).value.getAbsolutePath,
        "-doc-source-url",
        s"https://github.com/xuwei-k/play-ws-scalafix/tree/${hash}€{FILE_PATH}.scala"
      )
    },
    scalacOptions ++= PartialFunction
      .condOpt(CrossVersion.partialVersion(scalaVersion.value)) {
        case Some((2, v)) if v >= 12 =>
          Seq(
            "-Ywarn-unused:imports",
          )
      }
      .toList
      .flatten,
    scalacOptions ++= PartialFunction
      .condOpt(CrossVersion.partialVersion(scalaVersion.value)) {
        case Some((2, v)) if v <= 12 =>
          Seq(
            "-Yno-adapted-args",
            "-Xfuture",
          )
      }
      .toList
      .flatten,
    scalacOptions ++= List(
      "-deprecation",
      "-unchecked",
      "-Yrangepos",
      "-P:semanticdb:synthetics:on"
    )
  )
)

commonSettings
publish / skip := true

lazy val rules = project.settings(
  commonSettings,
  name := "play-ws-scalafix",
  libraryDependencies += "ch.epfl.scala" %% "scalafix-core" % V.scalafixVersion
)

lazy val input = project.settings(
  commonSettings,
  libraryDependencies += "com.typesafe.play" %% "play-ahc-ws" % "2.8.8",
  publish / skip := true
)

lazy val output = project.settings(
  commonSettings,
  libraryDependencies += "com.typesafe.play" %% "play-ahc-ws-standalone" % "2.1.3",
  libraryDependencies += "com.typesafe.play" %% "play-ws-standalone-json" % "2.1.3",
  libraryDependencies += "com.typesafe.play" %% "play-ws-standalone-xml" % "2.1.3",
  publish / skip := true
)

lazy val tests = project
  .settings(
    commonSettings,
    publish / skip := true,
    libraryDependencies += "ch.epfl.scala" % "scalafix-testkit" % V.scalafixVersion % Test cross CrossVersion.full,
    (Compile / compile) :=
      (Compile / compile).dependsOn(input / Compile / compile).value,
    scalafixTestkitOutputSourceDirectories :=
      (output / Compile / sourceDirectories).value,
    scalafixTestkitInputSourceDirectories :=
      (input / Compile / sourceDirectories).value,
    scalafixTestkitInputClasspath :=
      (input / Compile / fullClasspath).value,
  )
  .dependsOn(rules)
  .enablePlugins(ScalafixTestkitPlugin)
