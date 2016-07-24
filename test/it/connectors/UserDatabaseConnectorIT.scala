package it.connectors

import config.AsyncUnitSpec
import connectors.DefaultUserDatabaseConnector
import models.User
import org.scalatest.BeforeAndAfter
import reactivemongo.api.commands.LastError
import reactivemongo.core.errors.DatabaseException

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration

class UserDatabaseConnectorIT extends AsyncUnitSpec with BeforeAndAfter{
  val connector = new DefaultUserDatabaseConnector

  before {
    Await.ready(connector.clearUserFromDatabase(User("user", "password")), Duration.Inf)
    Await.ready(connector.createNewUser(User("user", "password")), Duration.Inf)
  }

  "createNewUser" must "return a successful write result after saving successfully" in {
    Await.ready(connector.clearUserFromDatabase(User("user", "password")), Duration.Inf)
    connector.createNewUser(User("user", "password"))
      .map(_.ok mustBe true)
  }
  it must "fail if attempting to create a user that already exists" in {
    val con = connector.createNewUser(User("user", "password"))
    recoverToExceptionIf[LastError](con).map{ e =>
      e.errmsg.get must include ("duplicate key error")
    }
  }
  it must "not be case sensetive for the userId" in {
    val con = connector.createNewUser(User("UsER", "password"))
    recoverToExceptionIf[LastError](con).map{ e =>
      e.errmsg.get must include ("duplicate key error")
    }
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
  it must "not be case sensetive for the userId" in {
    connector.validatePasswordForUser(User("USER", "password"))
      .map(_ mustBe true)
  }
}
