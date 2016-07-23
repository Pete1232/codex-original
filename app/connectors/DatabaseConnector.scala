package connectors

import models.Infantry

trait DatabaseConnector {
  def getUnitById(id: Int): Infantry
  def getAllUnits: List[Infantry]
}
