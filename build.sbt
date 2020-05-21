import ReleaseTransformations._

lazy val V = _root_.scalafix.sbt.BuildInfo

lazy val commonSettings = Def.settings(
  List(
    organization := "com.github.xuwei-k",
    homepage := Some(url("https://github.com/xuwei-k/play-ws-scalafix")),
    licenses := Seq("MIT License" -> url("https://opensource.org/licenses/mit-license")),
    description := "scalafix rule for replace old-play-ws to play-ws-standalone",
    scalaVersion := V.scala212,
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
    scalacOptions in (Compile, doc) ++= {
      val hash = sys.process.Process("git rev-parse HEAD").lineStream_!.head
      Seq(
        "-sourcepath",
        (baseDirectory in LocalRootProject).value.getAbsolutePath,
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
skip in publish := true

lazy val rules = project.settings(
  commonSettings,
  name := "play-ws-scalafix",
  libraryDependencies += "ch.epfl.scala" %% "scalafix-core" % V.scalafixVersion
)

lazy val input = project.settings(
  commonSettings,
  libraryDependencies += "com.typesafe.play" %% "play-ahc-ws" % "2.8.2",
  skip in publish := true
)

lazy val output = project.settings(
  commonSettings,
  libraryDependencies += "com.typesafe.play" %% "play-ahc-ws-standalone" % "2.1.2",
  libraryDependencies += "com.typesafe.play" %% "play-ws-standalone-json" % "2.1.2",
  libraryDependencies += "com.typesafe.play" %% "play-ws-standalone-xml" % "2.1.2",
  skip in publish := true
)

lazy val tests = project
  .settings(
    commonSettings,
    skip in publish := true,
    libraryDependencies += "ch.epfl.scala" % "scalafix-testkit" % V.scalafixVersion % Test cross CrossVersion.full,
    compile in Compile :=
      compile.in(Compile).dependsOn(compile.in(input, Compile)).value,
    scalafixTestkitOutputSourceDirectories :=
      sourceDirectories.in(output, Compile).value,
    scalafixTestkitInputSourceDirectories :=
      sourceDirectories.in(input, Compile).value,
    scalafixTestkitInputClasspath :=
      fullClasspath.in(input, Compile).value,
  )
  .dependsOn(rules)
  .enablePlugins(ScalafixTestkitPlugin)
