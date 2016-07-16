package config

import controllers.WebJarAssets
import org.scalatest.{AsyncFlatSpec, FlatSpec, MustMatchers}
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.mvc.Results
import play.api.test.FakeRequest

class UnitSpec extends FlatSpec with MustMatchers

class ControllerSpec extends AsyncFlatSpec with MustMatchers with Results {
  val application = new GuiceApplicationBuilder()
  implicit val webJarAssets = application.injector.instanceOf[WebJarAssets]

  val simpleRequest = FakeRequest()
}
