package controllers

import config.ControllerSpec
import connectors.UserDatabaseConnector
import play.api.i18n.{I18nSupport, Messages, MessagesApi}
import play.api.mvc.Session
import play.api.test.FakeRequest
import play.api.test.Helpers._

class ReauthenticationControllerSpec extends ControllerSpec with I18nSupport {
  implicit val messagesApi = application.injector.instanceOf[MessagesApi]
  implicit val userDatabaseConnector = application.injector.instanceOf[UserDatabaseConnector]

  val controller = new ReauthenticationController(userDatabaseConnector)

  val csrfTags = Map(
    "CSRF_TOKEN_NAME" -> "csrfToken",
    "CSRF_TOKEN_RE_SIGNED" -> "csrf-token-goes-here"
  )

  "reauth" must "load the login page" in running(application) {
    val result = controller.reauth()
      .apply(FakeRequest()
        .copyFakeRequest(tags = csrfTags)
        .withSession("userId" -> "user")
      )
    status(result) mustBe 200
    contentAsString(result) must include("<title>Reauthentication</title>")
  }
  it must "display the login form" in running(application) {
    val result = controller.reauth()
      .apply(FakeRequest()
        .copyFakeRequest(tags = csrfTags)
        .withSession("userId" -> "user")
      )
    val resultString = contentAsString(result)

    resultString must include regex ("""(<form)(.*)(id="loginForm")""".r)
    resultString must include("<input type=\"text\" id=\"userId\" name=\"userId\"")
    resultString must include("<input type=\"text\" id=\"password\" name=\"password\"")
    resultString must include regex (s"""<input type=\"hidden\" name=\"csrfToken\" value=\"${csrfTags.apply("CSRF_TOKEN_RE_SIGNED")}\"/>""".r)
  }
  it must "require the user to have a session" in {
    val result = controller.reauth()
      .apply(FakeRequest()
        .copyFakeRequest(tags = csrfTags)
      )
    status(result) mustBe 303
  }
  it must "return to the original request url after login" in {
    val result = controller.reauth(Some("/randomLocation"))
      .apply(FakeRequest()
        .copyFakeRequest(tags = csrfTags)
      )
    redirectLocation(result).get mustBe ("/login?continueUrl=%2FrandomLocation")
  }
  "reauthPost" must "return a form with errors if the form had missing data" in running(application) {
    val fieldRequiredResult = controller.reauthPost()
      .apply(FakeRequest()
        .copyFakeRequest(tags = csrfTags)
        .withSession("userId" -> "user")
      )
    val resultString = contentAsString(fieldRequiredResult)
    resultString must include("<div class=\"alert-message error\">")
    resultString must include(Messages("error.required", "userId"))
    resultString must include(Messages("error.required", "password"))
  }
  it must "return a form with errors if the credentials were wrong" in running(application) {
    val loginFailedResult = controller.reauthPost()
      .apply(FakeRequest()
        .copyFakeRequest(tags = csrfTags)
        .withFormUrlEncodedBody(
          "userId" -> "user",
          "password" -> "badPassword"
        )
        .withSession("userId" -> "user")
      )
    val resultString = contentAsString(loginFailedResult)
    resultString must include("<div class=\"alert-message error\">")
    resultString must include(Messages("login.validation.credentials"))
  }
  it must "redirect to home with a session cookie after login" in running(application) {
    val loginSuccessResult = controller.reauthPost()
      .apply(FakeRequest.apply()
        .withFormUrlEncodedBody(
          "userId" -> "user",
          "password" -> "password"
        )
        .withSession("userId" -> "user")
      )
    status(loginSuccessResult) mustBe 303
    redirectLocation(loginSuccessResult).get mustBe "/"
  }
  it must "redirect to the given url after login when provided" in running(application) {
    val loginSuccessResult = controller.reauthPost(Some("/account"))
      .apply(FakeRequest.apply()
        .withFormUrlEncodedBody(
          "userId" -> "user",
          "password" -> "password"
        )
        .withSession("userId" -> "user")
      )
    status(loginSuccessResult) mustBe 303
    redirectLocation(loginSuccessResult).get mustBe "/account"
  }
  it must "require a CSRF token to be present" in running(application){
    intercept[RuntimeException] {
      contentAsString(controller.reauthPost()
        .apply(simpleRequest
          .withSession("userId" -> "user")
        )
      )
    }.getMessage mustBe ("No CSRF token present!")
  }
  it must "require the user to have a session" in running(application) {
    val result = controller.reauthPost()
      .apply(FakeRequest()
        .copyFakeRequest(tags = csrfTags)
        .withFormUrlEncodedBody(
          "userId" -> "user",
          "password" -> "password"
        )
      )
    status(result) mustBe 303
    redirectLocation(result).get mustBe ("/login")
  }
  it must "return to the original request url after login" in {
    val result = controller.reauthPost(Some("/randomLocation"))
      .apply(FakeRequest()
        .copyFakeRequest(tags = csrfTags)
        .withFormUrlEncodedBody(
          "userId" -> "user",
          "password" -> "password"
        )
      )
    redirectLocation(result).get mustBe ("/login?continueUrl=%2FrandomLocation")
  }
  it must "add a flash cookie after successful reauthentication" in running(application) {
    val loginSuccessResult = controller.reauthPost()
      .apply(FakeRequest.apply()
        .withFormUrlEncodedBody(
          "userId" -> "user",
          "password" -> "password"
        )
        .withSession("userId" -> "user")
      )
    flash(loginSuccessResult).apply("REAUTH_SUCCESS") mustBe "true"
  }
  it must "only validate the currently logged in user" in running(application) {
    val loginSuccessResult = controller.reauthPost()
      .apply(FakeRequest.apply()
        .withFormUrlEncodedBody(
          "userId" -> "user2",
          "password" -> "password"
        )
        .copyFakeRequest(tags = csrfTags)
        .withSession("userId" -> "user")
      )
    status(loginSuccessResult) mustBe 400
  }
}
