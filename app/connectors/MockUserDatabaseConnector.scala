package connectors
import models.User

import scala.concurrent.Future

class MockUserDatabaseConnector extends UserDatabaseConnector{
  override def validatePasswordForUser(user: User): Future[Boolean] = {
    Future.successful(
      MockDatabase.userDb
      .applyOrElse(user.userId, (s: String) => "") == user.password
    )
  }
}
