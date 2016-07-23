package connectors
import models.{CombatRole, Infantry}
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.api.{MongoConnection, MongoDriver, ReadPreference}
import reactivemongo.bson.{BSONDocument, BSONDocumentReader}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

class DefaultDatabaseConnector extends DatabaseConnector{
  val mongoDriver: MongoDriver = new MongoDriver()
  val connection: MongoConnection = mongoDriver.connection(List("localhost"))

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

  def findById(collection: BSONCollection, id: Int): Future[Option[Infantry]] = {
    val query = BSONDocument("id" -> id)
    collection.find(query)
      .one[Infantry]
  }

  def findAll(collection: BSONCollection): Future[List[Infantry]] = {
    collection.find(BSONDocument())
      .sort(BSONDocument("id" -> 1))
      .cursor[Infantry](ReadPreference.Primary)
      .collect[List]()
  }

  override def getUnitById(id: Int): Infantry = {
    //TODO Should not be awaiting
    val result = findById(Await.result(infantryCollection, Duration.Inf), id)
    Await.result(result, Duration.Inf).get
  }

  override def getAllUnits: List[Infantry] = {
    val result = findAll(Await.result(infantryCollection, Duration.Inf))
    Await.result(result, Duration.Inf)
  }
}
