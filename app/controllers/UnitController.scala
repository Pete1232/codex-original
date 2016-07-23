package controllers

import com.google.inject.Inject
import connectors.DatabaseConnector
import play.api.Logger
import play.api.mvc.{Action, Controller}

import scala.concurrent.ExecutionContext.Implicits.global

class UnitController @Inject()(databaseConnector: DatabaseConnector)(implicit webJarAssets: WebJarAssets) extends Controller{
  def displayUnitDetails(id: Int) = Action.async {
    Logger.debug(s"Loading details for id $id")

    databaseConnector.getUnitById(id).map{maybeUnit =>
      Ok(views.html.unit_detail(maybeUnit.get))
    }
  }
}
