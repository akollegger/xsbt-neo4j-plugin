name := "xsbt-neo4j-plugin"

organization := "org.neo4j.tools"

version := "0.1.0"

scalaVersion := "2.9.1"

sbtPlugin := true

publishTo <<= (version) { version: String =>
      Some(Resolver.file("file", new File("/Users/akollegger/Developer/akollegger/mvn-repo") / {
        if  (version.trim.endsWith("SNAPSHOT"))  "snapshots/"
        else                                     "releases/" }    )
        (Patterns(true, Resolver.mavenStyleBasePattern))
        )
}

publishMavenStyle := true

libraryDependencies ~= { seq =>
  seq ++ Seq(
    "org.specs2" %% "specs2" % "1.8.2" % "test"
  )
}

initialCommands := "import org.neo4j.tools.xsbt._"


