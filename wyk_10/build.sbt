name := "wyk_10"
version := "1.0.0"

scalaVersion := "3.2.2"
// javaHome := Some(file("/home/michael/java/jdk-20.0.1/"))

scalacOptions ++= Seq(
   "-deprecation",         // Emit warning and location for usages of deprecated APIs.
   "-explain",             // Explain errors in more detail.
   "-feature",             // Emit warning and location for usages of features that should be imported explicitly.
   "-print-lines",         // Show source code line numbers.
   "-unchecked",           // Enable additional warnings where generated code depends on assumptions
   "-Xfatal-warnings",     // Fail the compilation if there are any warnings.
   "-Xmigration",          // Warn about constructs whose behavior may have changed since version.
   "-source:3.0",
  //  "-java-home", "/home/michael/java/jdk-20.0.1/"
   "-encoding", "utf8",
)

/*
  Currently available compiler options and their meanings can be looked-up in:

  https://github.com/lampepfl/dotty/blob/main/compiler/src/dotty/tools/dotc/config/ScalaSettings.scala
*/

libraryDependencies ++= {
  val akkaV = "2.6.20"
  Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaV,
    "ch.qos.logback" % "logback-classic" % "1.4.5" % Runtime,
    "com.typesafe.akka" %% "akka-slf4j" % akkaV
  )
}
