package connectors
import models.User

class MockUserDatabaseConnector extends UserDatabaseConnector{
  override def validatePasswordForUser(user: User): Boolean = {
    MockDatabase.userDb(user.userId) == user.password
  }
}
