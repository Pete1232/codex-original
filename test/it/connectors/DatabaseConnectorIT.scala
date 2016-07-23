package it.connectors

import config.AsyncUnitSpec
import connectors.DefaultDatabaseConnector

class DatabaseConnectorIT extends AsyncUnitSpec{
  val connector = new DefaultDatabaseConnector
  val expectedInfDBSize = 16

  "getUnitById" must "return the model for a guardian when called with id 112" in {
    connector.getUnitById(112).map(_.get.name mustBe "Guardian Defenders")
  }
  it must "return the model for a hawk when called with id 123" in {
    connector.getUnitById(123).map(_.get.name mustBe "Swooping Hawks")
  }
  "getAllUnits" must "return a set of all units in the database, ordered by id" in {
    connector.getAllUnits.map(_.size mustBe expectedInfDBSize)
  }
  it should "have a guardian defender at the head of the list" in {
    connector.getAllUnits.map(_.head.name mustBe "Guardian Defenders")
  }
  it should "have an avatar at the end of the list" in {
    connector.getAllUnits.map(_.last.name mustBe "Avatar of Khaine")
  }
}
