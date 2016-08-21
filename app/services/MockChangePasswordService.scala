package services

import connectors.DefaultUserDatabaseConnector
import reactivemongo.api.commands.UpdateWriteResult

import scala.concurrent.Future

class MockChangePasswordService extends DefaultUserDatabaseConnector with GenericPasswordChangeService{
  override def changePassword(userId: String, password: Array[Byte], salt: Array[Byte]): Future[UpdateWriteResult] = {
    Future.successful(UpdateWriteResult(true, 1, 2, Nil, Nil, None, None, None))
  }
}
