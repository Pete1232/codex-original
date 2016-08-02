package controllers

import javax.inject.Inject

import forms.UserForm
import play.Logger
import play.api.data.FormError
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, Controller}
import services.LoginService

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class LoginController @Inject()(loginService: LoginService)(implicit webJarAssets: WebJarAssets, val messagesApi: MessagesApi)
  extends Controller with I18nSupport{
  val userForm = new UserForm(loginService).userForm
  val login = Action{
    Ok(views.html.login(userForm)).withHeaders()
  }

  val loginPost = Action.async {implicit request =>
    userForm.bindFromRequest.fold(
      formWithErrors => {
        Logger.debug("Error creating form")
        formWithErrors.errors.foreach(error => Logger.info(s"Validation error on field ${error.messages}"))
        Future.successful(BadRequest(views.html.login(formWithErrors)))
      },
      userData => {
        Logger.debug("Validating user credentials")
        loginService.validateUser(userData).map { valid =>
          if(valid) {
            Logger.debug(s"Login successful ${userData.userId}")
            Redirect(routes.HomeController.home)
              .withSession("userId" -> userData.userId)
          }
          else {
            Logger.debug("Bad credentials - redirecting to login")
            val form = userForm.bindFromRequest.copy(errors = Seq(FormError("password","login.validation.credentials")))
            BadRequest(views.html.create(form))
          }
        }
      }
    )
  }
}
