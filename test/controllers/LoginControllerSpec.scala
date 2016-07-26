package controllers

import config.ControllerSpec
import play.api.i18n.{I18nSupport, Messages, MessagesApi}
import play.api.mvc.Session
import play.api.test.FakeRequest
import play.api.test.Helpers._
import services.LoginService

class LoginControllerSpec extends ControllerSpec with I18nSupport{
  implicit val messagesApi = application.injector.instanceOf[MessagesApi]
  implicit val loginService = application.injector.instanceOf[LoginService]
  val controller = new LoginController(loginService)
  val result = controller.login.apply(simpleRequest)
  val resultString = contentAsString(result)

  "login" must "load the login page" in {
    status(result) mustBe 200
    resultString must include("<title>Login</title>")
  }
  it must "display the login form" in {
    val resultString = contentAsString(result)
    resultString must include regex("""(<form)(.*)(id="loginForm")""".r)
    resultString must include("<input type=\"text\" id=\"userId\" name=\"userId\"")
    resultString must include("<input type=\"text\" id=\"password\" name=\"password\"")
  }
  "loginPost" must "return a form with errors if the form had missing data" in {
    val fieldRequiredResult = controller.loginPost.apply(simpleRequest)
    val resultString = contentAsString(fieldRequiredResult)
    resultString must include("<div class=\"alert-message error\">")
    resultString must include(Messages("error.required", "userId"))
    resultString must include(Messages("error.required", "password"))
  }
  it must "return a form with errors if the credentials were wrong" in {
    val loginFailedResult = controller.loginPost
      .apply(FakeRequest.apply()
        .withFormUrlEncodedBody(
          "userId" -> "user",
          "password" -> "badPassword"
        ))
    val resultString = contentAsString(loginFailedResult)
    resultString must include("<div class=\"alert-message error\">")
    resultString must include(Messages("login.validation.credentials"))
  }
  it must "redirect to home with a session cookie after login" in running(application) {
    val loginSuccessResult = controller.loginPost
      .apply(FakeRequest.apply()
        .withFormUrlEncodedBody(
          "userId" -> "user",
          "password" -> "password"
        ))
    status(loginSuccessResult) mustBe 303
    session(loginSuccessResult) mustBe (Session(Map("userId" -> "user")))
  }
}
