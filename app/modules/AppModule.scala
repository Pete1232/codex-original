package modules

import connectors.{DatabaseConnector, DefaultDatabaseConnector, MockDatabaseConnector}
import play.api.{Configuration, Environment}
import play.api.inject.{Binding, Module}
import services.{LoginService, MockLoginService}

class AppModule extends Module{
  override def bindings(environment: Environment, configuration: Configuration): Seq[Binding[_]] = Seq(
    bind[DatabaseConnector].to(new DefaultDatabaseConnector),
    bind[LoginService].to(new MockLoginService)
  )
}
