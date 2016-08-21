package services

import connectors.UserDatabaseConnector
import reactivemongo.api.commands.UpdateWriteResult

import scala.concurrent.Future

trait GenericPasswordChangeService {
  this: UserDatabaseConnector =>
  def changePassword(userId: String, password: Array[Byte], salt: Array[Byte]): Future[UpdateWriteResult]
}
