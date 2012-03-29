import java.io.FileOutputStream
import sbt._
import complete.Parser
import Keys._

// imports standard command parsing functionality
import complete.DefaultParsers._

object Neo4jBuild extends Build {

  val neo4jVersion = SettingKey[String]("neo4j-version")

  // a group of settings ready to be added to a Project
  // to automatically add them, do
  val neo4jSettings = Seq(
    neo4jVersion := "1.6.1"
  )

  lazy override val projects = Seq(root)

  lazy val root = Project(id="root",
      base=file("."),
      settings = Project.defaultSettings ++ Seq(
        commands ++= Seq(neo4jCommand)
    )
  )

  lazy val versionString = Space ~> charClass ({c => c.isLetterOrDigit | c.equals ('.') }).+ map { _.mkString }
  lazy val portString = Space ~> charClass ({c => c.isDigit }).+ map { _.mkString }
  lazy val installOp = token("install") ~ versionString.?
  lazy val startOp = token("start") ~ portString.?
  lazy val stopOp = token("stop")
  lazy val restartOp = token("restart")
  lazy val statusOp = token("status")
  lazy val neo4jOps = Space ~> (installOp | startOp | stopOp | restartOp | statusOp)

  val neo4jCommand = Command("neo4j", Help("neo4j", ("neo4j", "Control a local neo4j server"), """
        |Neo4j is here for you!
        |
        |These commands are available:
        |-----------------------------
        |install <version>          Download Neo4j to a local working directory
        |start <port>               Start serving up graph goodness with Neo4j
        |stop                       Stop the graph, for now
        |restart                    Bounce the server
        |status                     Is the server running?
        |
        |""".stripMargin))(_ => neo4jOps){ (state, args) =>

    args match {
      case ("install", v:Option[String]) => installNeo4j ( v.getOrElse("1.6.1"),  file("neo4j")  )
      case ("start", p:Option[String]) => println ("gonna start Neo4j on port " + p.getOrElse("7474"))
      case ("stop") => println ("gonna stop Neo4j")
      case ("restart") => println("Gonna restart Neo4j")
      case ("status") => println("Checking the status of Neo4j")
      case _ => println("What would *you* do with: " + args + "?")
    }
    state
  }
  
  def installNeo4j(version:String, where:File) {
    val edition = "community"
    
    if (!where.exists()) {
      where.mkdirs()
    }

    println("Installed Neo4j "+version+" into "+where.getAbsolutePath)
    url("http://dist.neo4j.org/neo4j-"+edition+"-"+version+"-unix.tar.gz") #> (where/"neo4j.tgz") !

    Process("tar" :: "-xzf" :: "neo4j.tgz" :: Nil, where) !

  }

}

