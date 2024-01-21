ThisBuild / scalaVersion     := "2.13.10"
ThisBuild / version          := "0.2.0"

val chiselVersion = "3.6.0"

lazy val root = (project in file("."))
    .settings(
        name := "RISC-SPM",
        libraryDependencies ++= Seq(
            "edu.berkeley.cs" %% "chisel3" % chiselVersion,
            "edu.berkeley.cs" %% "chiseltest" % "0.6.0" % "test"
        ),
        scalacOptions ++= Seq(
            "-language:reflectiveCalls",
            "-deprecation",
            "-feature",
            "-Xcheckinit",
        ),
        addCompilerPlugin("edu.berkeley.cs" % "chisel3-plugin" % chiselVersion cross CrossVersion.full),
    )

libraryDependencies += "org.scalatestplus" %% "junit-4-13" % "3.2.15.0" % "test"
