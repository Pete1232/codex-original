package it.controllers

import config.ControllerIT
import connectors.{DefaultUserDatabaseConnector, UserDatabaseConnector}
import controllers.CreateAccountController
import models.User
import play.api.i18n.{I18nSupport, Messages, MessagesApi}
import play.api.test.FakeRequest
import play.api.test.Helpers._

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class CreateAccountControllerIT extends ControllerIT with I18nSupport{
  implicit val messagesApi = application.injector.instanceOf[MessagesApi]
  override val controller = new CreateAccountController(application.injector.instanceOf[UserDatabaseConnector])
  val connector = new DefaultUserDatabaseConnector

  "createPost" must "reload the create account page if the user already exists" in running(application){
    Await.ready(connector.createNewUser(User("user", "password")), Duration.Inf)
    val createFailedResult = controller.createPost
      .apply(FakeRequest.apply()
        .withFormUrlEncodedBody(
          "userId" -> "user",
          "password" -> "password"
        ))
    status(createFailedResult) mustBe 400
  }
  it must "display a validation error when the user already exists" in running(application){
    Await.ready(connector.createNewUser(User("user", "password")), Duration.Inf)
    val createFailedResult = controller.createPost
      .apply(FakeRequest.apply()
        .withFormUrlEncodedBody(
          "userId" -> "user",
          "password" -> "password"
        ))
    contentAsString(createFailedResult) must include (Messages("login.validation.userExists"))
  }
}
