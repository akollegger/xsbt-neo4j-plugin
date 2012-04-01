package org.neo4j.tools.xsbt

import sbt._
import Keys._

import complete.DefaultParsers._

object Neo4jPlugin extends Plugin {

  override lazy val settings = Seq(commands += neo4jCommand,
    neo4jVersion := "1.6.1",
    neo4jHome := "neo4j"
  )

  val neo4jVersion = SettingKey[String]("neo4j-version")
  val neo4jHome = SettingKey[String]("neo4j-home")

  lazy val installOp = token("install")
  lazy val startOp = token("start")
  lazy val stopOp = token("stop")
  lazy val restartOp = token("restart")
  lazy val statusOp = token("status")
  lazy val neo4jOps = Space ~> (installOp | startOp | stopOp | restartOp | statusOp)

  val neo4jCommand = Command("neo4j", Help("neo4j", ("neo4j", "Control a local neo4j server"), """
        |Neo4j at your command
        |
        |These commands are available:
        |-----------------------------
        |install              Download Neo4j to a local working directory
        |start                Start serving up graph goodness with Neo4j
        |stop                 Stop the graph, for now
        |restart              Bounce the server
        |status               Is the server running?
        |
        |""".stripMargin))(_ => neo4jOps){ (state, args) =>

    import state._
    val extracted = Project.extract(state)
    import extracted._

    val neo4jVersionOpt: Option[String] = neo4jVersion in currentRef get structure.data
    val specifiedNeo4jVersion = neo4jVersionOpt.getOrElse("1.6.1")
    val neo4jHomeOpt: Option[String] = neo4jHome in currentRef get structure.data
    val specifiedNeo4jHome = file(neo4jHomeOpt.getOrElse("neo4j"))

    val fullyQualifiedNeo4j = "neo4j-community-"+specifiedNeo4jVersion

    args match {
      case ("install") => installNeo4j ( fullyQualifiedNeo4j,  specifiedNeo4jHome )
      case (cmd:String) => ctrlNeo4j( fullyQualifiedNeo4j, specifiedNeo4jHome, cmd )
      case _ => println("What would *you* do with: " + args + "?")
    }
    state
  }

  def installNeo4j(fullyQualifiedNeo4j:String, where:File) {

    if (!where.exists()) {
      println("Creating neo4j-home at: " + where.getAbsolutePath)
      where.mkdirs()
    }

    val neo4jArchive = fullyQualifiedNeo4j+"-unix.tar.gz"

    val downloadedArchive = (where/neo4jArchive)

    if (!downloadedArchive.exists()) {
      println("Downloading " + fullyQualifiedNeo4j)
      url("http://dist.neo4j.org/"+neo4jArchive) #> downloadedArchive !
    }

    val installedNeo4j = (where/fullyQualifiedNeo4j)
    if (!installedNeo4j.exists()) {
      println("Expanding " + downloadedArchive.getAbsolutePath)
      val success = Process("tar" :: "-xzf" :: downloadedArchive.getName :: Nil, where) !
    }

    if (installedNeo4j.exists()) {
      println("Neo4j installed at "+installedNeo4j.getAbsolutePath)
    }
  }

  def ctrlNeo4j(fullyQualifiedNeo4j:String, home:File, command:String ) {
    Process("neo4j" :: command :: Nil, home/fullyQualifiedNeo4j/"bin") !
  }

}


