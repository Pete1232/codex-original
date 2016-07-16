package controllers

import config.ControllerSpec
import play.api.test.Helpers._

class ArmyControllerSpec extends ControllerSpec{
  val controller = new ArmyController
  "displayArmyList" must "load the army list page" in {
    val result = controller.displayArmyList.apply(simpleRequest)
    status(result) mustBe 200
    contentAsString(result) must include("<title>Army Roster</title>")
  }
}
