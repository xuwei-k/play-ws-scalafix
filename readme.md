# play-ws migration tool

[![Build Status](https://travis-ci.org/xuwei-k/play-ws-scalafix.svg?branch=master)](https://travis-ci.org/xuwei-k/play-ws-scalafix)

<https://scalacenter.github.io/scalafix/docs/rules/external-rules.html>

`project/scalafix.sbt`

```scala
addSbtPlugin("ch.epfl.scala" % "sbt-scalafix" % "0.9.12")
```

sbt shell

```
> scalafixEnable
> scalafix dependency:PlayWS@com.github.xuwei-k:play-ws-scalafix:0.1.0
> test:scalafix dependency:PlayWS@com.github.xuwei-k:play-ws-scalafix:0.1.0
```
