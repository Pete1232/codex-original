package connectors
import models.{CombatRole, Infantry}
import reactivemongo.api.ReadPreference
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson.{BSONDocument, BSONDocumentReader}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class DefaultDatabaseConnector extends DatabaseConnector{

  implicit object InfantryReader extends BSONDocumentReader[Infantry] {
    def read(doc: BSONDocument): Infantry = {
      val id = doc.getAs[Int]("id").get
      val name = doc.getAs[String]("name").get
      val role = doc.getAs[String]("role").get

      Infantry(id, name, role = CombatRole.withName(role))
    }
  }

  def infantryCollection: Future[BSONCollection] =
    connection.database("codex").
      map(_.collection("infantry"))

  override def getUnitById(id: Int): Future[Option[Infantry]] = {
    val query = BSONDocument("id" -> id)
    infantryCollection.flatMap{
      _.find(query)
        .one[Infantry]
    }
  }

  override def getAllUnits: Future[List[Infantry]] = {
    infantryCollection.flatMap{
      _.find(BSONDocument())
        .sort(BSONDocument("id" -> 1))
        .cursor[Infantry](ReadPreference.Primary)
        .collect[List]()
    }
  }
}
