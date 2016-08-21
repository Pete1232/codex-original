package controllers

import config.ControllerSpec
import play.api.i18n.MessagesApi
import services.GenericPasswordChangeService

class ChangePasswordControllerSpec extends ControllerSpec {
  implicit val messagesApi = application.injector.instanceOf[MessagesApi]
  implicit val passwordChangeService = application.injector.instanceOf[GenericPasswordChangeService]

  val controller = new ChangePasswordController(passwordChangeService)

}
