package connectors

import config.AsyncUnitSpec
import models.User

class MockUserDatabaseConnectorSpec extends AsyncUnitSpec{
  val mockConnector = new MockUserDatabaseConnector
  val testDB = MockDatabase.db
  "validatePasswordForUser" must "return true if the password was correct" in {
    mockConnector
      .validatePasswordForUser(User("user", "password"))
      .map(_ mustBe true)
  }
  it must "return false if the password was incorrect" in {
    mockConnector.validatePasswordForUser(User("user", "p2ssword"))
      .map(_ mustBe false)
  }
  it must "return false if the user was not found in the database" in {
    mockConnector.validatePasswordForUser(User("notAUser", "password"))
      .map(_ mustBe false)
  }
}
