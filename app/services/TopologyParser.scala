package services

import scala.io.Source

object TopologyParser {
  val Lower = """([a-z])""".r
  val Upper = """([A-Z])""".r
  val Digit = """([0-9])""".r
  def parseTopology(password: String): String = {
    password.toCharArray
      .map {char =>
      char match {
        case Lower(_) => "?l"
        case Upper(_) => "?u"
        case Digit(_) => "?d"
        case _ => "?s"
      }
    }.mkString
  }

  def validateTopology(password: String): Boolean = {
    val topology = parseTopology(password)
    val filename = "conf/topology-blacklist.txt"
    val blacklist = Source.fromFile(filename).getLines()
    blacklist.forall(_ != topology)
  }
}
