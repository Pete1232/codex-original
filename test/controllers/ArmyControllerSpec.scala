package controllers

import config.ControllerSpec
import connectors.{DatabaseConnector, MockDatabase}
import play.api.test.Helpers._

class ArmyControllerSpec extends ControllerSpec{
  val controller = new ArmyController(application.injector.instanceOf[DatabaseConnector])
  val testDB = MockDatabase.db
  "displayArmyList" must "load the army list page" in {
    val result = controller.displayArmyList.apply(simpleRequest)
    status(result) mustBe 200
    contentAsString(result) must include("<title>Army Roster</title>")
  }
  it should "contain a link for each infantry in the database" in {
    val result = controller.displayArmyList.apply(simpleRequest)
    testDB.values.map {infantry =>
      contentAsString(result) must include regex(s"""(<a href="/army/${infantry.id})(.*)(${infantry.name}</a>)""".r)
    }
  }
}
