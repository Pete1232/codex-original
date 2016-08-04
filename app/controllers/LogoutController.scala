package controllers

import com.google.inject.Inject
import play.Logger
import play.api.mvc.{Action, Controller, DiscardingCookie}

/**
  * Controller for logout
  */
class LogoutController @Inject()(implicit webJarAssets: WebJarAssets) extends Controller {
  def logout = Action {
    Logger.debug("Logging out current user")
    Redirect(routes.HomeController.home)
      .discardingCookies(DiscardingCookie("PLAY_SESSION"))
  }
}
