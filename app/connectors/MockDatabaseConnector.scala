package connectors

import models.{CombatRole, Infantry}

object MockDatabaseConnector extends DatabaseConnector{
  val mockInfantryDatabase = Map(
    "guardian" -> Infantry(112, "Guardian Defender", CombatRole.Troops),
    "windrider" -> Infantry(114, "Windriders", CombatRole.Troops)
  )
  override def getUnitById(id: Int): Infantry = {
    mockInfantryDatabase.values.find(_.id==id).get
  }
}
