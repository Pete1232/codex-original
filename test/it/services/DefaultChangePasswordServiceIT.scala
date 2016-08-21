package it.services

import config.AsyncServiceSpec
import connectors.UserDatabaseConnector
import models.{DatabaseUser, User}
import org.scalatest.BeforeAndAfter
import reactivemongo.bson.BSONDocument
import services.DefaultChangePasswordService

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class DefaultChangePasswordServiceIT extends AsyncServiceSpec with BeforeAndAfter {
  val service = new DefaultChangePasswordService
  implicit val userDatabaseConnector = application.injector.instanceOf[UserDatabaseConnector]

  before {
    Await.ready(service.deleteUser(User("user", "password")), Duration.Inf)
    Await.ready(service.createNewUser(User("user", "password")), Duration.Inf)
  }

  "changePassword" must "update the given users password and salt with those provided" in {
    import service._

    val userId = "user"
    val password = "newpassword"
    val salt = "newsalt"

    service.changePassword(userId, password.getBytes(), salt.getBytes())
      .flatMap { result =>
        service.userCollection.flatMap {
          _.find(BSONDocument("userId" -> "user"))
            .one[DatabaseUser]
            .map(user => {
              (new String(user.get.password), new String(user.get.salt))
                .mustBe (password, salt)
            })
        }
      }
  }
}
