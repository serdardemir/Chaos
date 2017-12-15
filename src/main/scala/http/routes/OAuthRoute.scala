package http.routes

import akka.http.scaladsl.server.{Directives, Route}
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import http.handlers.OAuth2DataHandler
import http.oauth.OAuth2RouteProvider
import models.Account

class OAuthRoute(override val oauth2DataHandler: OAuth2DataHandler)
  extends Directives
    with OAuth2RouteProvider[Account]
    with FailFastCirceSupport {

  val route: Route = accessTokenRoute
}