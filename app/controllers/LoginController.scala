package controllers

import javax.inject.Inject
import forms.User
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, Controller}

class LoginController @Inject()(implicit webJarAssets: WebJarAssets, val messagesApi: MessagesApi)
  extends Controller with I18nSupport{
  val login = Action{
    Ok(views.html.login(User.userForm)).withHeaders()
  }
}
