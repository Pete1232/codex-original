package controllers

import config.ControllerSpec
import play.api.i18n.MessagesApi
import play.api.test.Helpers._

class CreateAccountControllerSpec extends ControllerSpec{
  implicit val messagesApi = application.injector.instanceOf[MessagesApi]
  val controller = new CreateAccountController
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
}
