name := """play-slick-tut"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  "com.typesafe.play"  %%  "play-slick" % "0.8.1"
)

resolvers += "Typesafe repository" at "https://repo.typesafe.com/typesafe/releases/"

