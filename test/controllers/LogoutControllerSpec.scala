package controllers

import config.ControllerSpec
import play.api.mvc.Cookie
import play.api.test.Helpers._

class LogoutControllerSpec extends ControllerSpec{
  val controller = new LogoutController()

  "logout" must "redirect to the home page" in {
    val response = controller.logout.apply(simpleRequest)
    status(response) mustBe 303
    redirectLocation(response).get mustBe "/"
  }
  it must "clear the users session cookie" in running(application){
    val result = controller.logout
      .apply(simpleRequest.withSession("userId" -> "user"))
    cookies(result).apply("PLAY_SESSION") mustBe Cookie("PLAY_SESSION","",Some(0))
  }
}
