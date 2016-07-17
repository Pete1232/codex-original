package connectors

import models.User

trait UserDatabaseConnector {
  def validatePasswordForUser(user: User): Boolean
}
