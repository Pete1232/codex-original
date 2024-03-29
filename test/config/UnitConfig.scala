package config

import connectors.{DatabaseConnector, MockDatabaseConnector, MockUserDatabaseConnector, UserDatabaseConnector}
import controllers.WebJarAssets
import org.scalatest.{AsyncFlatSpec, FlatSpec, MustMatchers}
import play.api.inject._
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.mvc.{Controller, Results}
import play.api.test.FakeRequest
import services.{GenericPasswordChangeService, MockChangePasswordService}

class UnitSpec extends FlatSpec with MustMatchers

class AsyncUnitSpec extends AsyncFlatSpec with MustMatchers

abstract class ControllerSpec extends UnitSpec with Results {
  val controller: Controller
  val application = new GuiceApplicationBuilder()
    .overrides(bind[DatabaseConnector].to[MockDatabaseConnector])
    .overrides(bind[UserDatabaseConnector].to[MockUserDatabaseConnector])
    .overrides(bind[GenericPasswordChangeService].to[MockChangePasswordService])
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

abstract class AsyncControllerSpec extends AsyncUnitSpec with Results {
  val controller: Controller
  val application = new GuiceApplicationBuilder()
    .overrides(bind[DatabaseConnector].to[MockDatabaseConnector])
    .build()
  implicit val webJarAssets = application.injector.instanceOf[WebJarAssets]

  val simpleRequest = FakeRequest()
}

abstract class AsyncServiceSpec extends AsyncUnitSpec with Results {
  val application = new GuiceApplicationBuilder()
    .overrides(bind[DatabaseConnector].to[MockDatabaseConnector])
    .build()
}