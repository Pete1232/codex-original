package connectors

import config.UnitSpec

class MockDatabaseConnectorSpec extends UnitSpec{
  val mockConnector = new MockDatabaseConnector
  val testDB = MockDatabase.db
  "getUnitById" must "return the model for the unit that has the given id" in {
    mockConnector.getUnitById(112) mustBe testDB("guardian")
    mockConnector.getUnitById(114) mustBe testDB("windrider")
  }
  "getAllUnits" must "return a set of all units in the database" in {
    mockConnector.getAllUnits mustBe testDB.values.toSet
  }
}
