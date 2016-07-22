package controllers

import config.ControllerSpec
import play.api.test.FakeRequest
import play.api.test.Helpers._

class AccountControllerSpec extends ControllerSpec{
  val controller = new AccountController()
  "displayUserDetails" must "allow an authenticated user to view the page" in running(application){
    val result = controller.displayUserDetails
      .apply(
        FakeRequest()
          .withSession("userId" -> "user")
      )
    status(result) mustBe 200
    contentAsString(result) must include("<title>Your Account</title>")
  }
  it should "redirect an unauthenticated user to login" in {
    val result = controller.displayUserDetails.apply(simpleRequest)
    status(result) mustBe 303
    redirectLocation(result).get mustBe ("/login")
  }
}
