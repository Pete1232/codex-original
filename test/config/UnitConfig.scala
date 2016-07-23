package config

import connectors.{DatabaseConnector, MockDatabaseConnector}
import controllers.WebJarAssets
import org.scalatest.{FlatSpec, MustMatchers}
import play.api.inject._
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.mvc.{Controller, Results}
import play.api.test.FakeRequest
import services.{LoginService, MockLoginService}

class UnitSpec extends FlatSpec with MustMatchers

abstract class ControllerSpec extends UnitSpec with Results {
  val controller: Controller
  val application = new GuiceApplicationBuilder()
    .overrides(bind[DatabaseConnector].to[MockDatabaseConnector])
    .overrides(bind[LoginService].to[MockLoginService])
    .build()
  implicit val webJarAssets = application.injector.instanceOf[WebJarAssets]

  val simpleRequest = FakeRequest()
}

abstract class ControllerIT extends UnitSpec with Results {
  val controller: Controller
  val application = new GuiceApplicationBuilder()
    .build()
  implicit val webJarAssets = application.injector.instanceOf[WebJarAssets]

  val simpleRequest = FakeRequest()
}