package controllers

import com.google.inject.Inject
import connectors.MockDatabaseConnector
import play.api.Logger
import play.api.mvc.{Action, Controller}

class ArmyController @Inject()(implicit webJarAssets: WebJarAssets) extends Controller{
  val armyToDisplay = MockDatabaseConnector.getAllUnits
  
  def displayArmyList = Action {
    Logger.debug("Loading army list")
    Ok(views.html.army_list(armyToDisplay))
  }
}
