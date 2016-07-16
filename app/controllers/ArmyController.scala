package controllers

import com.google.inject.Inject
import play.api.Logger
import play.api.mvc.{Action, Controller}

class ArmyController @Inject()(implicit webJarAssets: WebJarAssets) extends Controller{
  def displayArmyList = Action {
    Logger.debug("Loading army list")
    Ok(views.html.army_list())
  }
}
