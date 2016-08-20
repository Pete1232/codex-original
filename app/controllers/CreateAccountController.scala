package controllers

import com.google.inject.Inject
import connectors.UserDatabaseConnector
import forms.CreateAccountForm
import play.Logger
import play.api.data.FormError
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, Controller}
import reactivemongo.api.commands.LastError

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class CreateAccountController @Inject()(userDatabaseConnector: UserDatabaseConnector)(implicit webJarAssets: WebJarAssets, val messagesApi: MessagesApi)
  extends Controller with I18nSupport{
  val accountForm = new CreateAccountForm().form
  val create = Action { implicit request =>
    Ok(views.html.create(accountForm))
  }

  val createPost = Action.async { implicit request =>
    accountForm.bindFromRequest.fold(
      formWithErrors => {
        Logger.debug("Error creating form")
        formWithErrors.errors.foreach(error => Logger.info(s"Validation error on field ${error.key} ${error.messages}"))
        Future.successful(BadRequest(views.html.create(formWithErrors)))
      },
      userData => {
        Logger.debug(s"Creating user ${userData.userId}")
        userDatabaseConnector
          .createNewUser(userData)
          .map { writeResult =>
            Redirect(routes.HomeController.home)
              .withSession("userId" -> userData.userId)
          }
          .recover{
            case dbe: LastError => {
              Logger.debug(s"User already exists - redirecting back to sign-up page")
              val form = accountForm.bindFromRequest.copy(errors = Seq(FormError("userId","login.validation.userExists")))
              BadRequest(views.html.create(form))
            }
            case e: Exception => {
              Logger.error(s"Fatal exception $e during user creation")
              Redirect(routes.ErrorController.error)
            }
          }
      }
    )
  }
}
