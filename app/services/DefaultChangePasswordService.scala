package services

import connectors.DefaultUserDatabaseConnector
import play.Logger
import reactivemongo.api.commands.UpdateWriteResult
import reactivemongo.bson.BSONDocument

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class DefaultChangePasswordService extends DefaultUserDatabaseConnector with GenericPasswordChangeService {

  override def changePassword(userId: String, hashedAndSaltedPassword: Array[Byte], salt: Array[Byte]): Future[UpdateWriteResult] = {
    userCollection.flatMap { users =>
      users.update(
        BSONDocument(
          "userId" -> userId
        ),
        BSONDocument {
          "$set" -> BSONDocument(
            "password" -> hashedAndSaltedPassword,
            "salt" -> salt
          )
        },
        multi = false
      )
    }.map { success =>
      Logger.debug(s"update completed with result $success")
      success
    }
  }
}
