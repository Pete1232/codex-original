package controllers

import javax.inject._
import play.api.mvc._

/**
  * Controller for the landing page
  */
class HomeController @Inject()(implicit webJarAssets: WebJarAssets) extends Controller {

  def home = Action {
    Ok(views.html.home())
  }

}
