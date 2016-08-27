package controllers

import config.AsyncControllerSpec
import connectors.UserDatabaseConnector
import play.api.i18n.{I18nSupport, Messages, MessagesApi}
import play.api.mvc.Session
import play.api.test.FakeRequest
import play.api.test.Helpers._
import services.GenericPasswordChangeService

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class ChangePasswordControllerSpec extends AsyncControllerSpec with I18nSupport {
  implicit val messagesApi = application.injector.instanceOf[MessagesApi]
  implicit val passwordChangeService = application.injector.instanceOf[GenericPasswordChangeService]
  implicit val userDatabaseConnector = application.injector.instanceOf[UserDatabaseConnector]

  val controller = new ChangePasswordController(passwordChangeService, userDatabaseConnector)
  val loginController = new LoginController(userDatabaseConnector)

  val csrfTags = Map(
    "CSRF_TOKEN_NAME" -> "csrfToken",
    "CSRF_TOKEN_RE_SIGNED" -> "csrf-token-goes-here"
  )

  "changePassword" must "load the change password page" in running(application) {
    val result = controller.changePassword()
      .apply(FakeRequest()
        .copyFakeRequest(tags = csrfTags)
        .withSession("userId" -> "user")
      )
    status(result) mustBe 200
    contentAsString(result) must include("<title>Change Password</title>")
  }
  it must "display the change password form" in running(application) {
    val result = controller.changePassword()
      .apply(FakeRequest()
        .copyFakeRequest(tags = csrfTags)
        .withSession("userId" -> "user")
      )
    val resultString = contentAsString(result)

    resultString must include regex ("""(<form action="/change-password")(.*)(id="changePasswordForm")""".r)
    resultString must include("<input type=\"text\" id=\"oldPassword\" name=\"oldPassword\"")
    resultString must include("<input type=\"text\" id=\"newPassword\" name=\"newPassword\"")
    resultString must include("<input type=\"text\" id=\"newPasswordConfirm\" name=\"newPasswordConfirm\"")
    resultString must include regex (s"""<input type=\"hidden\" name=\"csrfToken\" value=\"${csrfTags.apply("CSRF_TOKEN_RE_SIGNED")}\"/>""".r)
  }
  it must "allow an authenticated user to view the page" in running(application) {
    val result = controller.changePassword
      .apply(
        FakeRequest()
          .copyFakeRequest(tags = csrfTags)
          .withSession("userId" -> "user")
      )
    status(result) mustBe 200
    contentAsString(result) must include("<title>Change Password</title>")
  }
  it should "redirect an unauthenticated user to login" in {
    val result = controller.changePassword.apply(simpleRequest)
    status(result) mustBe 303
    redirectLocation(result).get mustBe ("/login?continueUrl=%2Fchange-password")
  }

  "changePasswordPost" must "return a form with errors if the form had missing data" in running(application) {
    val fieldRequiredResult = controller.changePasswordPost()
      .apply(FakeRequest()
        .copyFakeRequest(tags = csrfTags)
        .withSession("userId" -> "user")
      )
    val resultString = contentAsString(fieldRequiredResult)
    resultString must include("<div class=\"alert-message error\">")
    resultString must include(Messages("error.required", "oldPassword"))
    resultString must include(Messages("error.required", "newPassword"))
    resultString must include(Messages("error.required", "newPasswordConfirm"))
  }
  it must "redirect to home without a session cookie after login" in running(application) {
    val changePasswordSuccessResult = controller.changePasswordPost()
      .apply(FakeRequest.apply()
        .copyFakeRequest(tags = csrfTags)
        .withFormUrlEncodedBody(
          "oldPassword" -> "password",
          "newPassword" -> "p2ssWord!",
          "newPasswordConfirm" -> "p2ssWord!"
        )
        .withSession("userId" -> "user")
      )
    status(changePasswordSuccessResult) mustBe 303
    redirectLocation(changePasswordSuccessResult).get mustBe "/"
    session(changePasswordSuccessResult) mustBe (Session(Map()))
  }
  it must "require a CSRF token to be present" in running(application) {
    intercept[RuntimeException] {
      contentAsString(controller.changePasswordPost()
        .apply(simpleRequest
          .withSession("userId" -> "user")
        )
      )
    }.getMessage mustBe ("No CSRF token present!")
  }
  it must "require the password and password check to match" in running(application) {
    val passwordMismatchResult = controller.changePasswordPost()
      .apply(FakeRequest.apply()
        .copyFakeRequest(tags = csrfTags)
        .withFormUrlEncodedBody(
          "oldPassword" -> "password",
          "newPassword" -> "p2ssWord!",
          "newPasswordConfirm" -> "p3ssWord!"
        )
        .withSession("userId" -> "user")
      )
    status(passwordMismatchResult) mustBe 400
    val resultString = contentAsString(passwordMismatchResult)
    resultString must include("<div class=\"alert-message error\">")
    resultString must include(Messages("login.validation.passwordMismatch"))
  }
  it should "redirect an unauthenticated user to login" in running(application) {
    val result = controller.changePasswordPost
      .apply(FakeRequest()
        .copyFakeRequest(tags = csrfTags)
        .withFormUrlEncodedBody(
          "oldPassword" -> "password",
          "newPassword" -> "p2ssWord!",
          "newPasswordConfirm" -> "p2ssWord!"
        )
      )
    status(result) mustBe 303
    redirectLocation(result).get mustBe ("/login?continueUrl=%2Fchange-password")
  }
}
