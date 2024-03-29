package controllers

import com.google.inject.Inject
import connectors.UserDatabaseConnector
import models.User
import play.Logger
import play.api.mvc.{Action, Controller}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class AccountController @Inject()(userDatabaseConnector: UserDatabaseConnector)(implicit webJarAssets: WebJarAssets) extends Controller{
  Logger.debug("Loading user details page")
  def displayUserDetails = Action { implicit request =>
    request2session.get("userId") match {
      case Some(userId) => Ok(views.html.account(userId))
      case _ => Redirect(routes.LoginController.login(Some("/account")))
    }
  }

  def deleteUser = Action.async { implicit request =>
    Logger.debug("Deleting current user from db")
    request2session.get("userId") match {
      case Some(userId) => {
        userDatabaseConnector.deleteUser(User(userId, ""))
          .map { success =>
            Logger.debug("User deleted successfully - removing session cookie")
            Redirect(routes.LogoutController.logout)
          }
          .recover{
            case e: Exception => {
              Logger.error(s"Fatal exception $e during user delete")
              Redirect(routes.ErrorController.error)
            }
          }
      }
      case _ => Future.successful(Redirect(routes.LoginController.login(Some("/account"))))
    }
  }
}
