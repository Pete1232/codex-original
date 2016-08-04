package services

import connectors.UserDatabaseConnector
import models.User

import scala.concurrent.Future

trait LoginService {
  this: UserDatabaseConnector =>
  def validateUser(user: User): Future[Boolean] = {
    validatePasswordForUser(user)
  }
  def vefiryUserExistence(user: User): Future[Boolean] = {
    verifyUserExists(user)
  }
}
