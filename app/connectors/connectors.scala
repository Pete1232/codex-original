import reactivemongo.api.{MongoConnection, MongoDriver}

package object connectors {
  val mongoDriver: MongoDriver = new MongoDriver()
  val connection: MongoConnection = mongoDriver.connection(List("localhost"))
}
