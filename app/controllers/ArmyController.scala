package controllers

import com.google.inject.Inject
import connectors.DatabaseConnector
import play.api.Logger
import play.api.mvc.{Action, Controller}

import scala.concurrent.ExecutionContext.Implicits.global

class ArmyController @Inject()(databaseConnector: DatabaseConnector)(implicit webJarAssets: WebJarAssets) extends Controller{
  val armyToDisplay = databaseConnector.getAllUnits
  
  def displayArmyList = Action.async {
    Logger.debug("Loading army list")
    armyToDisplay.map{armyList =>
      Ok(views.html.army_list(armyList))
    }
  }
}
