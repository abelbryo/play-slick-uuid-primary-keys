name := """play-slick-tut"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  "com.typesafe.play"  %%  "play-slick" % "0.8.1",
  "org.postgresql"     %   "postgresql" % "9.3-1102-jdbc5"
)

resolvers += "Typesafe repository" at "https://repo.typesafe.com/typesafe/releases/"

