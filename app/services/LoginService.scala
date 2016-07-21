package services

import connectors.UserDatabaseConnector
import models.User

trait LoginService {
  this: UserDatabaseConnector =>
  def validateUser(user: User): Boolean = {
    validatePasswordForUser(user)
  }
}
