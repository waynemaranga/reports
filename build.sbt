name := "reports"

version := "0.0.1"

scalaVersion := "3.6.4"

libraryDependencies ++= Seq(
  "org.tpolecat" %% "doobie-core" % "1.0.0-RC2",
  "org.tpolecat" %% "doobie-postgres" % "1.0.0-RC2",
  "org.tpolecat" %% "doobie-hikari" % "1.0.0-RC2",
  "com.softwaremill.sttp.client3" %% "core" % "3.9.0",
  "com.softwaremill.sttp.client3" %% "circe" % "3.9.0",
  "io.circe" %% "circe-generic" % "0.14.6",
  "com.typesafe" % "config" % "1.4.2",
  "io.github.cdimascio" % "dotenv-java" % "3.0.0",
  "org.postgresql" % "postgresql" % "42.7.0"  // Added explicit PostgreSQL driver
)