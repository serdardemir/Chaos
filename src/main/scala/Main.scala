import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import services.{CachingServiceImpl, KafkaIntegrationImpl, _}
import utils.Config

import scala.concurrent.ExecutionContext

object Main extends App with Config {

  implicit val actorSystem = ActorSystem()
  implicit val executor: ExecutionContext = actorSystem.dispatcher
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  val flywayService = new FlywayServiceImpl(jdbcUrl, dbUser, dbPassword)
  flywayService.migrateDatabaseSchema()

  val databaseService = new DatabaseServiceImpl(jdbcUrl, dbUser, dbPassword)
  val accountsService = new AccountsServiceImpl(databaseService)
  val oAuthClientsService = new OAuthClientsServiceImpl(databaseService, accountsService)
  val oAuthAccessTokensService = new OAuthAccessTokensServiceImpl(databaseService, oAuthClientsService)
  val cacheService = new CachingServiceImpl(redisHost, redisPort)
  val kafkaIntegration = new KafkaIntegrationImpl(kafkaBootstrapServers)




}
