package connectors

import config.UnitSpec
import models.User

class MockUserDatabaseConnectorSpec extends UnitSpec{
  val mockConnector = new MockUserDatabaseConnector
  val testDB = MockDatabase.db
  "validatePasswordForUser" must "return true if the password was correct" in {
    mockConnector.validatePasswordForUser(User("user", "password")) mustBe true
  }
  it must "return false if the password was incorrect" in {
    mockConnector.validatePasswordForUser(User("user", "p2ssword")) mustBe false
  }
  it must "return false if the user was not found in the database" in {
    mockConnector.validatePasswordForUser(User("notAUser", "password")) mustBe false
  }
}
