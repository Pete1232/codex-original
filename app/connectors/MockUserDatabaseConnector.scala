package connectors
import models.User
import reactivemongo.api.commands.{DefaultWriteResult, WriteResult}

import scala.concurrent.Future

class MockUserDatabaseConnector extends UserDatabaseConnector{
  override def validatePasswordForUser(user: User): Future[Boolean] = {
    Future.successful(
      MockDatabase.userDb
      .applyOrElse(user.userId, (s: String) => "") == user.password
    )
  }

  override def createNewUser(user: User): Future[WriteResult] = {
    user.userId match {
      case "fail" => Future.failed(new Exception)
      case _ => Future.successful(DefaultWriteResult(true, 1, Nil, None, None, None))
    }
  }
}
