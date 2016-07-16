package controllers

import config.ControllerSpec
import play.api.test.Helpers._

class HomeControllerSpec extends ControllerSpec{
  val controller = new HomeController

  "home" must "load the landing page" in {
    val result = controller.home.apply(simpleRequest)
    status(result) mustBe 200
    contentAsString(result) must include("<title>Home</title>")
  }
}
