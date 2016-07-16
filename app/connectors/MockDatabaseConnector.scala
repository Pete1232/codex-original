package connectors

import models.Infantry

class MockDatabaseConnector extends DatabaseConnector{
  override def getUnitById(id: Int): Infantry = {
    MockDatabase.db.values.find(_.id==id).get
  }

  override def getAllUnits: Set[Infantry] = {
    MockDatabase.db.values.toSet
  }
}
