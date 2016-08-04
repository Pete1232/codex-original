package it.connectors

import config.AsyncUnitSpec
import connectors.DefaultUserDatabaseConnector
import models.User
import org.apache.commons.codec.binary.Hex
import org.scalatest.BeforeAndAfter
import reactivemongo.api.commands.LastError

import scala.concurrent.Await
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

  "verifyUserExists" must "return true if the user exists" in {
    connector.verifyUserExists(User("user", "password"))
      .map(_ mustBe true)
  }
  it must "return false if the user does not exist" in {
    connector.verifyUserExists(User("notAUser", "password"))
      .map(_ mustBe false)
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

  "hashPassword" must "hash the given string using PBKDF2WithHmacSHA512" in {
    Hex.encodeHexString(connector.hashPassword("password", "salt".getBytes, 1, 512))
      .mustBe("867f70cf1ade02cff3752599a3a53dc4af34c7a669815ae5d513554e1c8cf252c02d470a285a0501bad999bfe943c08f050235d7d68b1da55e63f73b60a57fce")
    Hex.encodeHexString(connector.hashPassword("password", "salt".getBytes, 2, 64))
      .mustBe("e1d9c16aa681708a")
    Hex.encodeHexString(connector.hashPassword("password", "salt".getBytes, 4096, 64))
      .mustBe("d197b1b33db0143e")
  }

  "generateSalt" must "return a 64 bit salt" in {
    connector.generateSalt.length mustBe 8
  }
}
