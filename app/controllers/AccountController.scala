package controllers

import com.google.inject.Inject
import play.api.mvc.{Action, Controller}

class AccountController @Inject()(implicit webJarAssets: WebJarAssets) extends Controller{
  def displayUserDetails = Action { implicit request =>
    request2session.get("userId") match {
      case Some(userId) => Ok(views.html.account(userId))
      case _ => Redirect(routes.LoginController.login(Some("/account")))
    }
  }
}
