package controllers

import config.ControllerSpec
import connectors.UserDatabaseConnector
import play.api.test.FakeRequest
import play.api.test.Helpers._

class AccountControllerSpec extends ControllerSpec{
  implicit val userDatabaseConnector = application.injector.instanceOf[UserDatabaseConnector]
  val controller = new AccountController(userDatabaseConnector)
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
    redirectLocation(result).get mustBe ("/login?continueUrl=%2Faccount")
  }
  "deleteUser" must "redirect to the logout controller" in running(application){
    val result = controller.deleteUser.apply(simpleRequest.withSession("userId" -> "user"))
    status(result) mustBe 303
    redirectLocation(result).get mustBe "/logout"
  }
  it must "redirect to the login page if the session isn't found" in {
    val result = controller.deleteUser.apply(simpleRequest)
    status(result) mustBe 303
    redirectLocation(result).get mustBe "/login?continueUrl=%2Faccount"
  }
}
