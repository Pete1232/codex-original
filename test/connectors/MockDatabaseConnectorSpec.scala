package connectors

import config.AsyncUnitSpec

import scala.concurrent.ExecutionContext.Implicits.global

class MockDatabaseConnectorSpec extends AsyncUnitSpec{
  val mockConnector = new MockDatabaseConnector
  val testDB = MockDatabase.db
  "getUnitById" must "return the model for a guardian if called with id 122" in {
    mockConnector.getUnitById(112).map(_.get mustBe testDB("guardian"))
  }
  it must "return the model for a windrider if called with id 144" in {
    mockConnector.getUnitById(114).map(_.get mustBe testDB("windrider"))
  }
  "getAllUnits" must "return a set of all units in the database" in {
    mockConnector.getAllUnits.map(_ mustBe testDB.values.toList)
  }
}
