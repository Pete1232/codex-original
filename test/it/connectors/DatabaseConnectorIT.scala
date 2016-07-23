package it.connectors

import config.UnitSpec
import connectors.DefaultDatabaseConnector

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

class DatabaseConnectorIT extends UnitSpec{
  val connector = new DefaultDatabaseConnector
  val expectedInfDBSize = 16

  "getUnitById" must "return the model for the unit that has the given id" in {
    Await.result(connector.getUnitById(112), Duration.Inf).get.name mustBe "Guardian Defenders"
    Await.result(connector.getUnitById(123), Duration.Inf).get.name mustBe "Swooping Hawks"
  }
  "getAllUnits" must "return a set of all units in the database, ordered by id" in {
    Await.result(connector.getAllUnits, Duration.Inf).size mustBe expectedInfDBSize
    // test for expected first and last entries
    Await.result(connector.getAllUnits, Duration.Inf).head.name mustBe "Guardian Defenders"
    Await.result(connector.getAllUnits, Duration.Inf).last.name mustBe "Avatar of Khaine"
  }
}
