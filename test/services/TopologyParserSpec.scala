package services

import config.UnitSpec

class TopologyParserSpec extends UnitSpec{
  "parseTopology" must "return the topology of a given string" in {
    TopologyParser.parseTopology("Aa3!") mustBe "?u?l?d?s"
  }
  "validateTopology" must "return true if the given password isn't blacklisted" in {
    TopologyParser.validateTopology("!xdeSfg)j") mustBe true
  }
  it must "return false if the given password is blacklisted" in {
    TopologyParser.validateTopology("Abcde123") mustBe false
  }
}
