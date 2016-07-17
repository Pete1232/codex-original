package connectors

import com.google.inject.Singleton
import models.Infantry

@Singleton
class MockDatabaseConnector extends DatabaseConnector{
  override def getUnitById(id: Int): Infantry = {
    MockDatabase.db.values.find(_.id==id).get
  }

  override def getAllUnits: Set[Infantry] = {
    MockDatabase.db.values.toSet
  }
}
