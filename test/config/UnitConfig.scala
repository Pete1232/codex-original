package config

import connectors.{DatabaseConnector, MockDatabaseConnector}
import controllers.WebJarAssets
import org.scalatest.{FlatSpec, MustMatchers}
import play.api.inject._
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.mvc.{Controller, Results}
import play.api.test.FakeRequest

class UnitSpec extends FlatSpec with MustMatchers

abstract class ControllerSpec extends UnitSpec with Results {
  val controller: Controller
  val application = new GuiceApplicationBuilder()
    .overrides(bind[DatabaseConnector].to[MockDatabaseConnector])
    .build()
  implicit val webJarAssets = application.injector.instanceOf[WebJarAssets]

  val simpleRequest = FakeRequest()
}
