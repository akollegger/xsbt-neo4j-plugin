Neo4j sbt Plugin
================

The Neo4j sbt Plugin adds convenience methods for installing a local Neo4j server and controlling it
from within the sbt console. 

Requirements
------------

* [scala](http://scala-lang.org/) 2.9.1
* [sbt](https://github.com/harrah/xsbt/wiki) 0.11.2

These are awfully narrow requirements. Sorry about that. Broader applicability coming up soon.

Installation
------------

Add the following lines to 

  resolvers += "Kollegger Maven Repository" at "https://github.com/akollegger/mvn-repo/raw/master/snapshots/"

  addSbtPlugin("org.neo4j.tools" %% "xsbt-neo4j-plugin" % "0.1.0-SNAPSHOT")

Usage
-----

`help neo4j`

