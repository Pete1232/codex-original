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
      )
    status(result) mustBe 200
    contentAsString(result) must include("<title>Change Password</title>")
  }
  it must "display the change password form" in running(application) {
    val result = controller.changePassword()
      .apply(FakeRequest()
        .copyFakeRequest(tags = csrfTags)
      )
    val resultString = contentAsString(result)

    resultString must include regex ("""(<form action="/change-password")(.*)(id="changePasswordForm")""".r)
    resultString must include("<input type=\"text\" id=\"oldPassword\" name=\"oldPassword\"")
    resultString must include("<input type=\"text\" id=\"newPassword\" name=\"newPassword\"")
    resultString must include("<input type=\"text\" id=\"newPasswordConfirm\" name=\"newPasswordConfirm\"")
    resultString must include regex (s"""<input type=\"hidden\" name=\"csrfToken\" value=\"${csrfTags.apply("CSRF_TOKEN_RE_SIGNED")}\"/>""".r)
  }

  "changePasswordPost" must "return a form with errors if the form had missing data" in running(application) {
    val fieldRequiredResult = controller.changePasswordPost()
      .apply(FakeRequest()
        .copyFakeRequest(tags = csrfTags)
      )
    val resultString = contentAsString(fieldRequiredResult)
    resultString must include("<div class=\"alert-message error\">")
    resultString must include(Messages("error.required", "oldPassword"))
    resultString must include(Messages("error.required", "newPassword"))
    resultString must include(Messages("error.required", "newPasswordConfirm"))
  }
  it must "return a form with errors if the credentials were wrong" ignore running(application) {
    val changePasswordFailedResult = controller.changePasswordPost()
      .apply(FakeRequest()
        .copyFakeRequest(tags = csrfTags)
        .withFormUrlEncodedBody(
          "oldPassword" -> "badPassword",
          "newPassword" -> "p2ssWord!",
          "newPasswordConfirm" -> "p2ssWord!"
        )
        .withSession("userId" -> "user")
      )
    val resultString = contentAsString(changePasswordFailedResult)
    resultString must include("<div class=\"alert-message error\">")
    resultString must include(Messages("login.validation.oldpassword"))
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
  it must "require a CSRF token to be present" in {
    intercept[RuntimeException] {
      contentAsString(controller.changePasswordPost().apply(simpleRequest))
    }.getMessage mustBe ("No CSRF token present!")
  }
}
