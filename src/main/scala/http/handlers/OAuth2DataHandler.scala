package http.handlers

import models.Account
import services.{AccountsService, OAuthAccessTokensService, OAuthClientsService}

import scalaoauth2.provider._

class OAuth2DataHandler(
                         val oAuthClientsService: OAuthClientsService,
                         val oAuthAccessTokensService: OAuthAccessTokensService,
                         val accountsService: AccountsService
                       ) extends DataHandler[Account] {


  override def validateClient(maybeCredential: Option[ClientCredential], request: AuthorizationRequest) = ???

  override def getStoredAccessToken(authInfo: AuthInfo[Account]) = ???

  override def createAccessToken(authInfo: AuthInfo[Account]) = ???

  override def findUser(maybeCredential: Option[ClientCredential], request: AuthorizationRequest) = ???

  override def findAccessToken(token: String) = ???

  override def findAuthInfoByAccessToken(accessToken: AccessToken) = ???

  override def findAuthInfoByCode(code: String) = ???

  override def findAuthInfoByRefreshToken(refreshToken: String) = ???

  override def refreshAccessToken(authInfo: AuthInfo[Account], refreshToken: String) = ???

  override def deleteAuthCode(code: String) = ???

}
