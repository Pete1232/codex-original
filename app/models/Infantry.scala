package models

case class Infantry(id: Int,
                    name: String,
                    role: CombatRole.Value
                    //                      stats: CombatStats,
                    //                      gear: Set[CombatItem],
                    //                      rules: Set[Rule]
                     )

object CombatRole extends Enumeration {
  val HQ, Troops, Elites, FastAttack, HeavySupport, LordsOfWar = Value
}
