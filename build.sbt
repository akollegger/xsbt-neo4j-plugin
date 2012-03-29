name := "xsbt-neo4j"

organization := "org.neo4j.tools"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.9.1"

sbtPlugin := true

libraryDependencies ~= { seq =>
  val disVers = "0.8.7"
  seq ++ Seq(
    "org.specs2" %% "specs2" % "1.8.2" % "test",
    "net.databinder" %% "dispatch-core" % disVers,
    "net.databinder" %% "dispatch-oauth" % disVers,
    "net.databinder" %% "dispatch-nio" % disVers,
    /* Twine doesn't need the below dependencies, but it simplifies
     * the Dispatch tutorials to keep it here for now. */
    "net.databinder" %% "dispatch-http" % disVers,
    "net.databinder" %% "dispatch-tagsoup" % disVers,
    "net.databinder" %% "dispatch-jsoup" % disVers
  )
}


initialCommands := "import org.neo4j.tools.xsbt._"

seq(ScriptedPlugin.scriptedSettings: _*)

