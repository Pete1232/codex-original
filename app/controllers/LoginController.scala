package controllers

import javax.inject.Inject

import forms.UserForm
import play.Logger
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, Controller}
import services.LoginService

class LoginController @Inject()(loginService: LoginService)(implicit webJarAssets: WebJarAssets, val messagesApi: MessagesApi)
  extends Controller with I18nSupport{
  val userForm = new UserForm(loginService).userForm
  val login = Action{
    Ok(views.html.login(userForm)).withHeaders()
  }

  val loginPost = Action{implicit request =>
    userForm.bindFromRequest.fold(
      formWithErrors => {
        formWithErrors.errors.foreach(error => Logger.info(s"Validation error on field ${error.messages}"))
        BadRequest(views.html.login(formWithErrors))
      },
      userData => {
        Redirect(routes.HomeController.home)
          .withSession("userId" -> userData.userId)
      }
    )
  }
}
