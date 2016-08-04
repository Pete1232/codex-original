package connectors

import config.AsyncUnitSpec
import models.User
import org.scalatest.concurrent.ScalaFutures

class MockUserDatabaseConnectorSpec extends AsyncUnitSpec with ScalaFutures{
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
  "createNewUser" must "return a successful save result" in {
    mockConnector.createNewUser(User("user", "password"))
      .map(_.ok mustBe true)
  }
  it must "fail for userId 'fail' with a BadRequest exception" in {
    val result = mockConnector.createNewUser(User("fail", "password"))
    ScalaFutures.whenReady(result.failed){ e =>
      e mustBe a[Exception]
    }
  }
  "verifyUserExists" must "return true if the user exists" in {
    mockConnector
      .verifyUserExists(User("user", "password"))
      .map(_ mustBe true)
  }
  it must "return false if the user does not exist" in {
    mockConnector
      .verifyUserExists(User("notAUser", "password"))
      .map(_ mustBe false)
  }
}
