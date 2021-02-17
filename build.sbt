name := "functional-programming-in-scala"
scalaVersion := "2.13.3"

lazy val fpInScala = Project(id = "fp-in-scala", base = file("."))
  .settings(settings)
  .aggregate(
    chapter2
  )

lazy val chapter2 = Project(id = "chapter-2", base = file("chapter-2"))
  .settings(
    name := "chapter-2",
    settings
  )

lazy val settings = Seq(
  version := "0.0.1",
  organization := "agondek",
  scalaVersion := "2.13.3",
  // From: https://github.com/DavidGregory084/mill-tpolecat/blob/master/itest/src/scala213/build.sc
  scalacOptions ++= Seq(
    "-encoding",
    "utf8",
    "-Xsource:3",
    "-explaintypes",
    "-feature",
    "-language:existentials",
    "-language:experimental.macros",
    "-language:higherKinds",
    "-language:implicitConversions",
    "-unchecked",
    "-Xcheckinit",
    "-Xfatal-warnings",
    "-Xlint:adapted-args",
    "-Xlint:constant",
    "-Xlint:delayedinit-select",
    "-Xlint:deprecation",
    "-Xlint:doc-detached",
    "-Xlint:inaccessible",
    "-Xlint:infer-any",
    "-Xlint:missing-interpolator",
    "-Xlint:nullary-unit",
    "-Xlint:option-implicit",
    "-Xlint:package-object-classes",
    "-Xlint:poly-implicit-overload",
    "-Xlint:private-shadow",
    "-Xlint:stars-align",
    "-Xlint:type-parameter-shadow",
    "-Wunused:nowarn",
    "-Wdead-code",
    "-Wextra-implicit",
    "-Wnumeric-widen",
    "-Wunused:implicits",
    "-Wunused:explicits",
    "-Wunused:imports",
    "-Wunused:locals",
    "-Wunused:params",
    "-Wunused:patvars",
    "-Wunused:privates",
    "-Wvalue-discard"
  ),
)
