package controllers

import config.ControllerSpec
import play.api.test.Helpers._

class LoginControllerSpec extends ControllerSpec{
  val controller = new LoginController()
  "GET login" must "load the login page" in {
    val result = controller.login.apply(simpleRequest)
    status(result) mustBe 200
    contentAsString(result) must include("<title>Login</title>")
  }
}
