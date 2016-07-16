package controllers

import com.google.inject.Inject
import connectors.MockDatabaseConnector
import play.api.mvc.{Action, Controller}

class UnitController @Inject()(implicit webJarAssets: WebJarAssets) extends Controller{
  def unitToDisplay(id: Int) = MockDatabaseConnector.getUnitById(id)

  def displayUnitDetails(id: Int) = Action {
    Ok(views.html.unit_detail(unitToDisplay(id)))
  }
}
