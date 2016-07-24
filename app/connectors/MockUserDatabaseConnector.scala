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

  override def createNewUser(user: User): Future[User] = {
    user.userId match {
      case "fail" => Future.failed(new Exception)
      case _ => Future.successful(user)
    }
  }
}
