package controllers

import config.ControllerSpec
import play.api.test.Helpers._

class AccountControllerSpec extends ControllerSpec{
  val controller = new AccountController()
  "displayUserDetails" must "load the user details page" in {
    val result = controller.displayUserDetails.apply(simpleRequest)
    status(result) mustBe 200
    contentAsString(result) must include("<title>Your Account</title>")
  }
}
