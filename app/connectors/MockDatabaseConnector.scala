package connectors

import models.{CombatRole, Infantry}

object MockDatabaseConnector extends DatabaseConnector{
  val mockInfantryDatabase = Map(
    "guardian" -> Infantry(112, "Guardian Defenders", CombatRole.Troops),
    "windrider" -> Infantry(114, "Windriders", CombatRole.Troops),
    "fire_dragons" -> Infantry(119, "Fire Dragons", CombatRole.Elites)
  )
  override def getUnitById(id: Int): Infantry = {
    mockInfantryDatabase.values.find(_.id==id).get
  }

  override def getAllUnits: Set[Infantry] = {
    mockInfantryDatabase.values.toSet
  }
}
