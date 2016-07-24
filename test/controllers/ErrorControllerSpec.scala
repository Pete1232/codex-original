package controllers

import config.ControllerSpec
import play.api.test.Helpers._

class ErrorControllerSpec extends ControllerSpec{
  val controller = new ErrorController
  "error" must "load the error page" in {
    val result = controller.error.apply(simpleRequest)
    status(result) mustBe 500
    contentAsString(result) must include("<title>Error</title>")
  }
}
