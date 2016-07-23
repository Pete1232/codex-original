package connectors

import com.google.inject.Singleton
import models.Infantry

import scala.concurrent.Future

@Singleton
class MockDatabaseConnector extends DatabaseConnector{
  override def getUnitById(id: Int): Future[Option[Infantry]] = {
    Future.successful(Some(MockDatabase.db.values.find(_.id==id).get))
  }

  override def getAllUnits: Future[List[Infantry]] = {
    Future.successful(MockDatabase.db.values.toList)
  }
}
