package connectors

import models.{CombatRole, Infantry}

object MockDatabase {
  val db = Map(
    "guardian" -> Infantry(112, "Guardian Defenders", CombatRole.Troops),
    "windrider" -> Infantry(114, "Windriders", CombatRole.Troops),
    "fire_dragons" -> Infantry(119, "Fire Dragons", CombatRole.Elites)
  )

  val userDb = Map(
    "user" -> "password"
  )
}
