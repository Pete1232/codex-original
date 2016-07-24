package controllers

import config.ControllerSpec
import connectors.UserDatabaseConnector
import play.api.i18n.{I18nSupport, Messages, MessagesApi}
import play.api.mvc.Session
import play.api.test.FakeRequest
import play.api.test.Helpers._

class CreateAccountControllerSpec extends ControllerSpec with I18nSupport{
  implicit val messagesApi = application.injector.instanceOf[MessagesApi]
  val mockUserDatabaseConnector = application.injector.instanceOf[UserDatabaseConnector]
  val controller = new CreateAccountController(mockUserDatabaseConnector)
  val result = controller.create.apply(simpleRequest)
  val resultString = contentAsString(result)

  "createAccount" must "display the create account page" in {
    status(result) mustBe 200
    resultString must include("<title>Create Account</title>")
  }
  it must "display the create account form" in {
    val resultString = contentAsString(result)
    resultString must include regex("""(<form)(.*)(id="createAccountForm">)""".r)
    resultString must include("<input type=\"text\" id=\"userId\" name=\"userId\"")
    resultString must include("<input type=\"text\" id=\"password\" name=\"password\"")
  }
  "createPost" must "return a form with errors if the form had missing data" in {
    val fieldRequiredResult = controller.createPost.apply(simpleRequest)
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
          "password" -> "password"
        ))
    status(createSuccessResult) mustBe 303
    session(createSuccessResult) mustBe (Session(Map("userId" -> "user")))
  }
  it must "redirect to the page with errors if the save failed" in running(application){
    val createFailedResult = controller.createPost
      .apply(FakeRequest.apply()
        .withFormUrlEncodedBody(
          "userId" -> "fail",
          "password" -> "password"
        ))
    status(createFailedResult) mustBe 303
    redirectLocation(createFailedResult).get mustBe ("/error")
  }
}
