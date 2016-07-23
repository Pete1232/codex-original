package connectors

import config.UnitSpec

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class MockDatabaseConnectorSpec extends UnitSpec{
  val mockConnector = new MockDatabaseConnector
  val testDB = MockDatabase.db
  "getUnitById" must "return the model for the unit that has the given id" in {
    Await.result(mockConnector.getUnitById(112), Duration.Inf).get mustBe testDB("guardian")
    Await.result(mockConnector.getUnitById(114), Duration.Inf).get mustBe testDB("windrider")
  }
  "getAllUnits" must "return a set of all units in the database" in {
    Await.result(mockConnector.getAllUnits, Duration.Inf) mustBe testDB.values.toList
  }
}
