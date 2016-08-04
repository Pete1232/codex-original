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
import scala.util.Success

class LoginController @Inject()(loginService: LoginService)(implicit webJarAssets: WebJarAssets, val messagesApi: MessagesApi)
  extends Controller with I18nSupport{
  val userForm = new UserForm(loginService).userForm
  def login(continueUrl: Option[String] = None) = Action{
    Logger.debug(s"Setting continueUrl to ${continueUrl.getOrElse("/")}")
    Ok(views.html.login(userForm, continueUrl)).withHeaders()
  }

  def loginPost(continueUrl: Option[String] = None) = Action.async {implicit request =>
    userForm.bindFromRequest.fold(
      formWithErrors => {
        Logger.debug("Error creating form")
        formWithErrors.errors.foreach(error => Logger.info(s"Validation error on field ${error.messages}"))
        Future.successful(BadRequest(views.html.login(formWithErrors)))
      },
      userData => {
        Logger.debug("Validating user credentials")
        loginService.vefiryUserExistence(userData)
            .flatMap{_ => loginService.validateUser(userData)}
            .map { valid =>
            if(valid) {
              Logger.debug(s"Login successful ${userData.userId}")
              val continue = continueUrl.getOrElse("/")
              Logger.debug(s"Redirecting to $continue")
              Redirect(continue)
                .withSession("userId" -> userData.userId)
            }
            else {
              Logger.debug("Bad credentials - redirecting to login")
              val form = userForm.bindFromRequest.copy(errors = Seq(FormError("password","login.validation.credentials")))
              BadRequest(views.html.login(form))
            }
          }
      }
    )
  }
}
