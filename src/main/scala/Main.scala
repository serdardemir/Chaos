import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import services.FlywayServiceImpl
import utils.Config

import scala.concurrent.ExecutionContext

object Main extends App with Config {

  implicit val actorSystem = ActorSystem()
  implicit val executor: ExecutionContext = actorSystem.dispatcher
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  val flywayService = new FlywayServiceImpl(jdbcUrl, dbUser, dbPassword)

}
