package http.handlers

import models.{Account, OAuthAccessToken}
import services.{AccountsService, _}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scalaoauth2.provider.{ClientCredentialsRequest, InvalidClient, PasswordRequest, _}


class OAuth2DataHandler(
                         val oAuthClientsService: OAuthClientsService,
                         val oAuthAccessTokensService: OAuthAccessTokensService,
                         val accountsService: AccountsService
                       ) extends DataHandler[Account] {


  override def validateClient(maybeCredential: Option[ClientCredential], request: AuthorizationRequest):Future[Boolean] =
  {
    maybeCredential.fold(Future.successful(false))(
      clientCredential=>oAuthClientsService.validate(
        clientCredential.clientId,
        clientCredential.clientSecret.getOrElse(""),request.grantType
      )
    )
  }

  override def getStoredAccessToken(authInfo: AuthInfo[Account]):Future[Option[AccessToken]] = {
    oAuthAccessTokensService.findByAuthorized(authInfo.user,authInfo.clientId.getOrElse("")).map(_.map(toAccessToken))
  }

  private val accessTokenExpireSeconds = 3600

  private  def toAccessToken(accessToken:OAuthAccessToken)=
  {
    AccessToken(accessToken.accessToken,
      Some(accessToken.refreshToken),
      None,
      Some(accessTokenExpireSeconds),
      accessToken.createdAt
    )
  }

  override def createAccessToken(authInfo: AuthInfo[Account]):
  Future[AccessToken] = {
    authInfo.clientId.fold(Future.failed[AccessToken](new InvalidRequest()))
      {
        clientId =>
          (
            for{
              clientOpt <- oAuthClientsService.findByClientId(clientId)
              toAccessToken <- oAuthAccessTokensService.create(authInfo.user,
                clientOpt.get).map(toAccessToken)
              if clientOpt.isDefined
            } yield toAccessToken).recover{case _=>throw new InvalidRequest()}
      }

  }

  override def findUser(maybeCredential: Option[ClientCredential], request: AuthorizationRequest) = ???

  override def findAccessToken(token: String) = ???

  override def findAuthInfoByAccessToken(accessToken: AccessToken) = ???

  override def findAuthInfoByCode(code: String) = ???

  override def findAuthInfoByRefreshToken(refreshToken: String) = ???

  override def refreshAccessToken(authInfo: AuthInfo[Account], refreshToken: String) = ???

  override def deleteAuthCode(code: String) = ???

}
