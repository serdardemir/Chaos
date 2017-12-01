package services

import org.flywaydb.core.Flyway

trait FlywayService {

  def migrateDatabaseSchema(): Unit

  def dropDatabase(): Unit
}

class FlywayServiceImpl(jdbcUrl: String, dbUser: String, dbPassword: String) extends FlywayService {
  private[this] val flyway = new Flyway()
  flyway.setDataSource(jdbcUrl, dbUser, dbPassword)
  flyway.setInitOnMigrate(true)

  override def migrateDatabaseSchema() = flyway.migrate()

  override def dropDatabase() = flyway.clean()
}