package config

import controllers.WebJarAssets
import org.scalatest.{FlatSpec, MustMatchers}
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.mvc.{Controller, Results}
import play.api.test.FakeRequest

class UnitSpec extends FlatSpec with MustMatchers

abstract class ControllerSpec extends UnitSpec with Results {
  val controller: Controller
  val application = new GuiceApplicationBuilder()
  implicit val webJarAssets = application.injector.instanceOf[WebJarAssets]

  val simpleRequest = FakeRequest()
}
