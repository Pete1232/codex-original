package controllers

import javax.inject.Inject

import connectors.UserDatabaseConnector
import forms.UserForm
import play.Logger
import play.api.data.FormError
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, Controller}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class ReauthenticationController @Inject()(userDatabaseConnector: UserDatabaseConnector)
                                          (implicit webJarAssets: WebJarAssets, val messagesApi: MessagesApi)
  extends Controller with I18nSupport{
  val userForm = new UserForm(userDatabaseConnector).userForm
  def reauth(continueUrl: Option[String] = None) = Action{ implicit request =>
    request2session.get("userId") match {
      case Some(userId) => {
        Logger.debug(s"Setting continueUrl to ${continueUrl.getOrElse("/")}")
        Ok(views.html.reauth(userForm, continueUrl)).withHeaders()
      }
      case _ => Redirect(routes.LoginController.login(continueUrl))
    }
  }

  def reauthPost(continueUrl: Option[String] = None) = Action.async {implicit request =>
    request2session.get("userId") match {
      case Some(userId) => {
        userForm.bindFromRequest.fold(
          formWithErrors => {
            Logger.debug("Error creating form")
            formWithErrors.errors.foreach(error => Logger.info(s"Validation error on field ${error.messages}"))
            Future.successful(BadRequest(views.html.reauth(formWithErrors)))
          },
          userData => {
            if(userId == userData.userId) {
              Logger.debug("Validating user credentials")
              userDatabaseConnector.verifyUserExists(userData)
                .flatMap { _ => userDatabaseConnector.validatePasswordForUser(userData) }
                .map { valid =>
                  if (valid) {
                    Logger.debug(s"Login successful ${userData.userId}")
                    val continue = continueUrl.getOrElse("/")
                    Logger.debug(s"Redirecting to $continue")
                    Redirect(continue)
                      .flashing("REAUTH_SUCCESS" -> "true")
                  }
                  else {
                    Logger.debug("Bad credentials - redirecting to login")
                    val form = userForm.bindFromRequest.copy(errors = Seq(FormError("password", "login.validation.credentials")))
                    BadRequest(views.html.reauth(form))
                  }
                }
            }
            else {
              val form = userForm.bindFromRequest.copy(errors = Seq(FormError("password", "login.validation.unauthorised")))
              Future.successful(BadRequest(views.html.reauth(form)))
            }
          }
        )
      }
      case _ => Future.successful(Redirect(routes.LoginController.login(continueUrl)))
    }
  }
}
