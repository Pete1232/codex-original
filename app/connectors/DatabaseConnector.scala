package connectors

import models.Infantry

import scala.concurrent.Future

trait DatabaseConnector {
  def getUnitById(id: Int): Future[Option[Infantry]]
  def getAllUnits: List[Infantry]
}
