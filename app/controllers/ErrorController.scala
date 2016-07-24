package controllers

import com.google.inject.Inject
import play.Logger
import play.api.mvc.{Action, Controller}

class ErrorController  @Inject()(implicit webJarAssets: WebJarAssets) extends Controller{
  def error = Action {
    Logger.debug("Loading app error page")
    InternalServerError(views.html.error())
  }
}
