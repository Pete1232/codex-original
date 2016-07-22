package controllers

import com.google.inject.Inject
import play.api.mvc.{Action, Controller}

class AccountController @Inject()(implicit webJarAssets: WebJarAssets) extends Controller{
  def displayUserDetails = Action {
    Ok(views.html.account())
  }
}
