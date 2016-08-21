package connectors

import models.User
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.api.commands.WriteResult

import scala.concurrent.Future

trait UserDatabaseConnector {
  def userCollection: Future[BSONCollection]
  def validatePasswordForUser(user: User): Future[Boolean]
  def verifyUserExists(user: User): Future[Boolean]
  def createNewUser(user: User): Future[WriteResult]
  def deleteUser(user: User): Future[WriteResult]
}
