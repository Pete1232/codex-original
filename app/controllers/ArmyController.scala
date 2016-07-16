package controllers

import com.google.inject.Inject
import connectors.DatabaseConnector
import play.api.Logger
import play.api.mvc.{Action, Controller}

class ArmyController @Inject()(databaseConnector: DatabaseConnector)(implicit webJarAssets: WebJarAssets) extends Controller{
  val armyToDisplay = databaseConnector.getAllUnits
  
  def displayArmyList = Action {
    Logger.debug("Loading army list")
    Ok(views.html.army_list(armyToDisplay))
  }
}
