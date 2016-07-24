package it.connectors

import config.AsyncUnitSpec
import connectors.DefaultUserDatabaseConnector
import models.User
import org.scalatest.BeforeAndAfter

import scala.concurrent.{Await, Future}
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
  it must "not be case sensetive for the userId" in {
    val user1 = connector.createNewUser(User("user", "password"))
    val user2 = connector.createNewUser(User("USER", "password"))
    val user3 = connector.createNewUser(User("UsER", "password"))
    val users = Seq(user1, user2, user3)

    Future.sequence(users).map{
      _ mustBe Seq(User("user", "password"), User("user", "password"), User("user", "password"))
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
