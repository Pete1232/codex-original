package connectors
import models.User

class MockUserDatabaseConnector extends UserDatabaseConnector{
  override def validatePasswordForUser(user: User): Boolean = {
    MockDatabase.userDb
      .applyOrElse(user.userId, (s: String) => "") == user.password
  }
  override def isKnownUser(user: User): Boolean =
    MockDatabase.userDb.contains(user.userId)
}
