package services

import connectors.DefaultUserDatabaseConnector
import play.Logger
import reactivemongo.api.BSONSerializationPack.Writer
import reactivemongo.api.commands.UpdateWriteResult
import reactivemongo.bson.BSONDocument

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class ChangePasswordService extends DefaultUserDatabaseConnector with GenericUserService {

  def changePassword(userId: String, password: Array[Byte], salt: Array[Byte]): Future[UpdateWriteResult] = {
    userCollection.flatMap { users =>
      users.update(
        BSONDocument(
          "userId" -> userId
        ),
        BSONDocument {
          "$set" -> BSONDocument(
            "password" -> password,
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
