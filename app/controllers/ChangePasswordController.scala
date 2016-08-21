package controllers

import javax.inject.Inject

import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.Controller
import services.GenericPasswordChangeService

class ChangePasswordController @Inject()(passwordChangeService: GenericPasswordChangeService)
                                        (implicit webJarAssets: WebJarAssets, val messagesApi: MessagesApi)
  extends Controller with I18nSupport {

}
