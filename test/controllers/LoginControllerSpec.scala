package controllers

import config.ControllerSpec
import play.api.i18n.MessagesApi
import play.api.test.Helpers._

class LoginControllerSpec extends ControllerSpec{
  implicit val messagesApi = application.injector.instanceOf[MessagesApi]
  val controller = new LoginController
  val result = controller.login.apply(simpleRequest)
  val resultString = contentAsString(result)

  "GET login" must "load the login page" in {
    status(result) mustBe 200
    resultString must include("<title>Login</title>")
  }
  it must "display the login form" in {
    val resultString = contentAsString(result)
    resultString must include("<form action=\"/login\" method=\"GET\" id=\"loginForm\">")
    resultString must include("<input type=\"text\" id=\"userId\" name=\"userId\"")
    resultString must include("<input type=\"text\" id=\"password\" name=\"password\"")
  }
}
