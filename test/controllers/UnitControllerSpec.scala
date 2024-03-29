package controllers

import config.ControllerSpec
import connectors.DatabaseConnector
import play.api.test.Helpers._

class UnitControllerSpec extends ControllerSpec{
  val controller = new UnitController(application.injector.instanceOf[DatabaseConnector])

  "displayUnitdetails" must "load the unit details page for the given id" in {
    val result112 = controller.displayUnitDetails(112).apply(simpleRequest)
    status(result112) mustBe 200
    contentAsString(result112) must include("<title>Unit detail</title>")
    contentAsString(result112) must include("Guardian Defenders")

    val result114 = controller.displayUnitDetails(114).apply(simpleRequest)
    status(result114) mustBe 200
    contentAsString(result114) must include("<title>Unit detail</title>")
    contentAsString(result114) must include("Windriders")

    val result119 = controller.displayUnitDetails(119).apply(simpleRequest)
    status(result119) mustBe 200
    contentAsString(result119) must include("<title>Unit detail</title>")
    contentAsString(result119) must include("Fire Dragons")
  }
}
