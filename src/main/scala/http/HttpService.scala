package http

import akka.http.scaladsl.server.Directives.pathPrefix
import http.handlers.OAuth2DataHandler
import http.routes.OAuthRoute
import services.{AccountsService, CachingService, OAuthAccessTokensService, OAuthClientsService}
import utils.CorsSupport

import scala.concurrent.ExecutionContext

class HttpService(
                   oAuthClientsService: OAuthClientsService,
                   oAuthAccessTokensService: OAuthAccessTokensService,
                   accountsService: AccountsService,
                   cacheService: CachingService
                 )(implicit executionContext: ExecutionContext) extends CorsSupport {

  val oAuth2DataHandler = new OAuth2DataHandler(oAuthClientsService, oAuthAccessTokensService, accountsService)
  val oauthRouter = new OAuthRoute(oAuth2DataHandler)


  val routes =
    corsHandler {
      oauthRouter.route
    }
}
