package controllers

import javax.inject._

import play.Logger
import play.api.mvc._

/**
  * Controller for the landing page
  */
class HomeController @Inject()(implicit webJarAssets: WebJarAssets) extends Controller {
  def home = Action {
    Logger.debug("Loading app home page")
    Ok(views.html.home())
  }
}
