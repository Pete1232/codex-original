package connectors
import com.google.inject.Singleton
import models.User

@Singleton
class MockUserDatabaseConnector extends UserDatabaseConnector{
  override def validatePasswordForUser(user: User): Boolean = {
    MockDatabase.userDb(user.userId) == user.password
  }
}
