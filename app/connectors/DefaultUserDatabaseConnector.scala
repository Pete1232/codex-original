package connectors
import models.User
import play.Logger
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.api.commands.WriteResult
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
      _.find(BSONDocument("userId" -> user.userId))
        .one[User]
    }.map{ _ match {
        case Some(foundUser) => foundUser.password == user.password
        case None => false
      }
    }
  }

  override def createNewUser(user: User): Future[User] = {
    userCollection.flatMap { users =>
      users.find(BSONDocument("userId" -> user.userId))
        .one[User]
        .map {
          _ match {
            case Some(foundUser) => {
              Logger.debug(s"User ${foundUser.userId} found - no further action")
            }
            case None => {
              Logger.debug(s"No user ${user.userId} found - creating new user")
              users.insert(BSONDocument("userId" -> user.userId, "password" -> user.password))
            }
          }
        }
    }.map{success =>
      user
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