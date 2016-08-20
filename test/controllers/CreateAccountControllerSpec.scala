package controllers

import config.ControllerSpec
import connectors.UserDatabaseConnector
import play.api.i18n.{I18nSupport, Messages, MessagesApi}
import play.api.mvc.Session
import play.api.test.FakeRequest
import play.api.test.Helpers._

class CreateAccountControllerSpec extends ControllerSpec with I18nSupport {
  implicit val messagesApi = application.injector.instanceOf[MessagesApi]
  val mockUserDatabaseConnector = application.injector.instanceOf[UserDatabaseConnector]
  val controller = new CreateAccountController(mockUserDatabaseConnector)
  val csrfTags = Map(
    "CSRF_TOKEN_NAME" -> "csrfToken",
    "CSRF_TOKEN_RE_SIGNED" -> "csrf-token-goes-here"
  )

  "createAccount" must "display the create account page" in {
    val result = controller.create
      .apply(FakeRequest()
        .copyFakeRequest(tags = csrfTags)
      )
    status(result) mustBe 200
    contentAsString(result) must include("<title>Create Account</title>")
  }
  it must "display the create account form" in {
    val result = controller.create
      .apply(FakeRequest()
        .copyFakeRequest(tags = csrfTags)
      )
    val resultString = contentAsString(result)
    resultString must include regex ("""(<form)(.*)(id="createAccountForm")""".r)
    resultString must include("<input type=\"text\" id=\"userId\" name=\"userId\"")
    resultString must include("<input type=\"text\" id=\"password\" name=\"password\"")
  }
  "createPost" must "return a form with errors if the form had missing data" in {
    val fieldRequiredResult = controller.createPost
      .apply(FakeRequest()
        .copyFakeRequest(tags = csrfTags)
      )
    val resultString = contentAsString(fieldRequiredResult)
    resultString must include("<div class=\"alert-message error\">")
    resultString must include(Messages("error.required", "userId"))
    resultString must include(Messages("error.required", "password"))
  }
  it must "redirect to home with a session cookie if the save succeeded" in running(application) {
    val createSuccessResult = controller.createPost
      .apply(FakeRequest.apply()
        .withFormUrlEncodedBody(
          "userId" -> "user",
          "password" -> "p2SsworD!"
        ))
    status(createSuccessResult) mustBe 303
    session(createSuccessResult) mustBe (Session(Map("userId" -> "user")))
  }
  it must "redirect to the page with errors if the save failed" in running(application) {
    val createFailedResult = controller.createPost
      .apply(FakeRequest.apply()
        .withFormUrlEncodedBody(
          "userId" -> "fail",
          "password" -> "p2SsworD!"
        ))
    status(createFailedResult) mustBe 303
    redirectLocation(createFailedResult).get mustBe ("/error")
  }
  it must "display the password strength tips if the password was blacklisted" in {
    val createBadTopologyResult = controller.createPost
      .apply(FakeRequest.apply()
        .copyFakeRequest(tags = csrfTags)
        .withFormUrlEncodedBody(
          "userId" -> "fail",
          "password" -> "password"
        ))
    contentAsString(createBadTopologyResult) must include(Messages("login.validation.topology.help.header"))
    contentAsString(createBadTopologyResult) must include(Messages("login.validation.topology.help.content"))
  }
  it must "not display the password strength tips otherwise" in {
    val result = controller.create
      .apply(FakeRequest()
        .copyFakeRequest(tags = csrfTags)
      )
    contentAsString(result) must not include (Messages("login.validation.topology.help.header"))
    contentAsString(result) must not include (Messages("login.validation.topology.help.content"))
  }
  it must "require a CSRF token to be present" in {
    intercept[RuntimeException] {
      contentAsString(controller.createPost().apply(simpleRequest))
    }.getMessage mustBe ("No CSRF token present!")
  }
}
