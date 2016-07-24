package controllers

import com.google.inject.Inject
import connectors.UserDatabaseConnector
import forms.CreateAccountForm
import play.Logger
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc.{Action, Controller}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class CreateAccountController @Inject()(userDatabaseConnector: UserDatabaseConnector)(implicit webJarAssets: WebJarAssets, val messagesApi: MessagesApi)
  extends Controller with I18nSupport{
  val accountForm = new CreateAccountForm().form
  val create = Action {
    Ok(views.html.create(accountForm))
  }

  val createPost = Action.async { implicit request =>
    accountForm.bindFromRequest.fold(
      formWithErrors => {
        formWithErrors.errors.foreach(error => Logger.info(s"Validation error on field ${error.messages}"))
        Future.successful(BadRequest(views.html.create(formWithErrors)))
      },
      userData => {
        userDatabaseConnector
          .createNewUser(userData)
          .map { savedUser =>
            Redirect(routes.HomeController.home)
              .withSession("userId" -> savedUser.userId)
          }
          .recover{
            case e: Exception => Redirect(routes.ErrorController.error)
          }
      }
    )
  }
}
