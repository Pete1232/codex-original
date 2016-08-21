package modules

import connectors._
import play.api.inject.{Binding, Module}
import play.api.{Configuration, Environment}
import services.{DefaultChangePasswordService, GenericPasswordChangeService}

class AppModule extends Module{
  override def bindings(environment: Environment, configuration: Configuration): Seq[Binding[_]] = Seq(
    bind[DatabaseConnector].to(new DefaultDatabaseConnector),
    bind[UserDatabaseConnector].to(new DefaultUserDatabaseConnector),
    bind[GenericPasswordChangeService].to(new DefaultChangePasswordService)
  )
}
