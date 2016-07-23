package connectors

import models.User

import scala.concurrent.Future

trait UserDatabaseConnector {
  def validatePasswordForUser(user: User): Future[Boolean]
}
