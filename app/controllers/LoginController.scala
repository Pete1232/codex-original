package controllers

import javax.inject.Inject

import forms.User
import forms.User.userForm
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, Controller}

class LoginController @Inject()(implicit webJarAssets: WebJarAssets, val messagesApi: MessagesApi)
  extends Controller with I18nSupport{
  val login = Action{
    Ok(views.html.login(userForm)).withHeaders()
  }

  val loginPost = Action{implicit request =>
    userForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.login(formWithErrors))
      },
      userData => {
        // TODO user should be in the models package
        val newUser = forms.User(userData.userId, userData.password)
        Redirect(routes.HomeController.home)
      }
    )
  }
}
