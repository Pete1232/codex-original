package connectors
import models.User
import play.Logger
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.api.commands.{UpdateWriteResult, WriteResult}
import reactivemongo.bson.{BSONDocument, BSONDocumentReader}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class DefaultUserDatabaseConnector extends UserDatabaseConnector{

  implicit object InfantryReader extends BSONDocumentReader[User] {
    def read(doc: BSONDocument): User = {
      val userId = doc.getAs[String]("userId").get
      val password = doc.getAs[String]("password").get

      User(userId, password)
    }
  }

  def userCollection: Future[BSONCollection] =
    connection.database("codex").
      map(_.collection("users"))

  override def validatePasswordForUser(user: User): Future[Boolean] = {
    userCollection.flatMap{
      _.find(BSONDocument("userId" -> user.userId.toLowerCase))
        .one[User]
    }.map{ _ match {
        case Some(foundUser) => foundUser.password == user.password
        case None => false
      }
    }
  }

  override def createNewUser(user: User): Future[User] = {
    val formattedUserId = user.userId.toLowerCase
    userCollection.flatMap { users =>
      users.update(
        BSONDocument("userId" -> formattedUserId),
        BSONDocument("userId" -> formattedUserId, "password" -> user.password),
        upsert = true
      )
    }.map{success =>
      Logger.debug(s"process completed with result $success")
      User(formattedUserId, user.password)
    }
  }

  /**
    * For use in testing
    */
  def clearUserFromDatabase(user: User): Future[WriteResult] = {
    userCollection.flatMap{
      _.remove(BSONDocument("userId" -> "user"))
    }
  }
}