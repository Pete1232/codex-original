package controllers

import com.google.inject.Inject

import play.Logger
import play.api.mvc.{Action, Controller}

/**
  * Controller for the landing page
  */
class HomeController @Inject()(implicit webJarAssets: WebJarAssets) extends Controller {
  def home = Action {
    Logger.debug("Loading app home page")
    Ok(views.html.home())
  }
}
