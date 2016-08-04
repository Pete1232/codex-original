package connectors
import java.security.SecureRandom
import java.util.Arrays
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

import models.{DatabaseUser, User}
import play.Logger
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.api.commands.WriteResult
import reactivemongo.api.indexes.{Index, IndexType}
import reactivemongo.bson.{BSONDocument, BSONDocumentReader}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class DefaultUserDatabaseConnector extends UserDatabaseConnector{

  implicit object InfantryReader extends BSONDocumentReader[DatabaseUser] {
    def read(doc: BSONDocument): DatabaseUser = {
      val userId = doc.getAs[String]("userId").get
      val password = doc.getAs[Array[Byte]]("password").get
      val salt = doc.getAs[Array[Byte]]("salt").get

      DatabaseUser(userId, password, salt)
    }
  }

  def userCollection: Future[BSONCollection] =
    connection.database("codex").
      map(_.collection("users"))


  override def validatePasswordForUser(user: User): Future[Boolean] = {
    userCollection.flatMap{
      _.find(BSONDocument("userId" -> user.userId.toLowerCase))
        .one[DatabaseUser]
    }.map{ _ match {
        case Some(storedUser) => {
          Arrays.equals(
            storedUser.password,
            hashPassword(user.password, storedUser.salt)
          )
        }
        case None => false
      }
    }
  }

  override def createNewUser(user: User): Future[WriteResult] = {
    val formattedUserId = user.userId.toLowerCase
    userCollection.flatMap { users =>
      val salt = generateSalt
      users.indexesManager.ensure(Index(Seq("userId" -> IndexType.Text), unique = true))
      users.insert(
        BSONDocument(
          "userId" -> formattedUserId,
          "password" -> hashPassword(user.password, salt),
          "salt" -> salt
        )
      )
    }.map{success =>
      Logger.debug(s"process completed with result $success")
      success
    }
  }

  def hashPassword(password: String, salt: Array[Byte], iterations: Int = 4096, keyLength: Int = 256): Array[Byte] = {
    Logger.debug("Hashing password")
    SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512")
      .generateSecret(
        new PBEKeySpec(password.toCharArray, salt, iterations, keyLength)
      ).getEncoded
  }

  def generateSalt: Array[Byte] = {
    Logger.debug("Generating new salt")
    val byteArray: Array[Byte] = new Array(8)
    new SecureRandom()
      .nextBytes(byteArray)
    byteArray
  }

  /**
    * For use in testing
    */
  def clearUserFromDatabase(user: User): Future[WriteResult] = {
    Logger.debug(s"Clearing user ${user.userId} from database")
    userCollection.flatMap{
      _.remove(BSONDocument("userId" -> user.userId.toLowerCase()))
    }
  }

  override def verifyUserExists(user: User): Future[Boolean] = {
    Logger.debug("Verifying user existence")
    val query = BSONDocument("userId" -> user.userId)
    userCollection.flatMap{
      _.find(query)
        .one
    }.map {
      _.isDefined
    }
  }
}