package controllers

import com.google.inject.Inject
import play.api.mvc.{Action, Controller}

class LoginController @Inject()(implicit webJarAssets: WebJarAssets) extends Controller{
  val login = Action{
    Ok(views.html.login())
  }
}
