# play-ws migration tool

[![Build Status](https://travis-ci.com/xuwei-k/play-ws-scalafix.svg?token=x41ypGcpoAvqgrjpqRwV&branch=master)](https://travis-ci.com/xuwei-k/play-ws-scalafix)

<https://scalacenter.github.io/scalafix/docs/rules/external-rules.html>
<https://github.com/playframework/playframework/blob/98eed7c34ef33136cb7/documentation/manual/releases/release26/migration26/WSMigration26.md>

`project/scalafix.sbt`

```scala
addSbtPlugin("ch.epfl.scala" % "sbt-scalafix" % "0.9.21")
```

sbt shell

```
> scalafixEnable
> scalafixAll dependency:PlayWS@com.github.xuwei-k:play-ws-scalafix:0.1.1
```
