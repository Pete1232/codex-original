package modules

import connectors.{DatabaseConnector, MockDatabaseConnector}
import play.api.{Configuration, Environment}
import play.api.inject.{Binding, Module}

class DatabaseModule extends Module{
  override def bindings(environment: Environment, configuration: Configuration): Seq[Binding[_]] = Seq(
    // TODO Implement default database connector - leave as mock for now
    bind[DatabaseConnector].to(new MockDatabaseConnector)
  )
}
