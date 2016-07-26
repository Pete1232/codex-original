package it.controllers

import config.ControllerIT
import connectors.DatabaseConnector
import controllers.ArmyController
import play.api.test.Helpers._

class ArmyControllerIT extends ControllerIT{
  // to test the module injection is working correctly
  val controller = new ArmyController(application.injector.instanceOf[DatabaseConnector])
  "displayArmyList" must "load the army list page" in {
    val result = controller.displayArmyList.apply(simpleRequest)
    status(result) mustBe 200
    contentAsString(result) must include("<title>Army Roster</title>")
  }
  it should "contain a link for each infantry in the database" in {
    val result = controller.displayArmyList.apply(simpleRequest)
    // test first and last value
    contentAsString(result) must include regex(s"""(<a href="/army/112")(.*)(Guardian Defenders</a>)""".r)
    contentAsString(result) must include regex(s"""(<a href="/army/137")(.*)(Avatar of Khaine</a>)""".r)
  }
}
