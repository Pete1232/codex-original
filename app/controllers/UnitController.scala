package controllers

import com.google.inject.Inject
import connectors.DatabaseConnector
import play.api.Logger
import play.api.mvc.{Action, Controller}

class UnitController @Inject()(databaseConnector: DatabaseConnector)(implicit webJarAssets: WebJarAssets) extends Controller{
  def unitToDisplay(id: Int) = databaseConnector.getUnitById(id)

  def displayUnitDetails(id: Int) = Action {
    Logger.debug(s"Loading details for id $id")
    Ok(views.html.unit_detail(unitToDisplay(id)))
  }
}
