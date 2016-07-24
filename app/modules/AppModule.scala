package modules

import connectors._
import play.api.{Configuration, Environment}
import play.api.inject.{Binding, Module}
import services.{LoginService, MockLoginService}

class AppModule extends Module{
  override def bindings(environment: Environment, configuration: Configuration): Seq[Binding[_]] = Seq(
    bind[DatabaseConnector].to(new DefaultDatabaseConnector),
    bind[UserDatabaseConnector].to(new MockUserDatabaseConnector),
    bind[LoginService].to(new MockLoginService)
  )
}
