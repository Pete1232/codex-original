package connectors

import config.UnitSpec

class MockDatabaseConnectorSpec extends UnitSpec{
  val testDB = MockDatabaseConnector.mockInfantryDatabase
  "getUnitById" should "return the model for the unit that has the given id" in {
    MockDatabaseConnector.getUnitById(112) mustBe testDB("guardian")
    MockDatabaseConnector.getUnitById(114) mustBe testDB("windrider")
  }
}
