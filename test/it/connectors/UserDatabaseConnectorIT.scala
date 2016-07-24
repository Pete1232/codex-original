package it.connectors

import config.AsyncUnitSpec
import connectors.DefaultUserDatabaseConnector
import models.User
import org.scalatest.BeforeAndAfter

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class UserDatabaseConnectorIT extends AsyncUnitSpec with BeforeAndAfter{
  val connector = new DefaultUserDatabaseConnector

  before {
    Await.ready(connector.clearUserFromDatabase(User("user", "password")), Duration.Inf)
    Await.ready(connector.createNewUser(User("user", "password")), Duration.Inf)
  }

  "createNewUser" must "return the user after saving successfully" in {
    connector.createNewUser(User("user", "password"))
      .map(_ mustBe User("user", "password"))
  }

  "validatePasswordForUser" must "return true if the users password matches the db" in {
    connector.validatePasswordForUser(User("user", "password"))
      .map(_ mustBe true)
  }
  it must "return false if the user is not found" in {
    connector.validatePasswordForUser(User("notAUser", "password"))
      .map(_ mustBe false)
  }
  it must "return false if the given password did not match the database" in {
    connector.validatePasswordForUser(User("user", "notTheirPassword"))
      .map(_ mustBe false)
  }
}
