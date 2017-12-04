package services

import models.{Account, OAuthClient, OAuthClientsTable}

import scala.concurrent.{ExecutionContext, Future}

trait OAuthClientsService extends OAuthClientsTable{

  def create(client:OAuthClient) :Future[OAuthClient]

  def validate(clientId:String,clientSecret:String, grantType:String):Future[Boolean]

  def findClientCredentials(clientId:String,clientSecret:String):Future[Option[Account]]

  def findClientByClientId(clientId:Long) :Future[Option[OAuthClient]]

  def findByClientId(clientId:String):Future[Option[OAuthClient]]

  def tableQ()=oauthClients

}

class  OAuthClientsServiceImpl(
                                val databaseService: DatabaseService,
                                val accountsService: AccountsService
                              )(implicit executionContext: ExecutionContext)
  extends OAuthClientsService {

  import databaseService._
  import databaseService.driver.api._

  override def create(client: OAuthClient):Future[OAuthClient] = db.run(oauthClients returning  oauthClients+=client)

  override def validate(clientId: String, clientSecret: String, grantType: String) :Future[Boolean]={
    db.run(oauthClients.filter(x=>x.clientId===clientId&&x.clientSecret===clientSecret).result)
      .map(_.headOption.exists(y=>y.grantType==grantType||grantType=="refresh_token"))
  }

  override def findClientCredentials(clientId: String, clientSecret: String):Future[Option[Account]] = {
    for{
      accountId<- db.run(oauthClients.filter(x=>x.clientId===clientId&&x.clientSecret===clientSecret).result).map(_.headOption.map(_.ownerId))
      account<- accountsService.findAccountById(accountId.get)
    }
      yield  account
  }

  override def findByClientId(clientId: String):Future[Option[OAuthClient]] = db.run(oauthClients.filter(_.clientId === clientId).result.headOption)



  override def findClientByClientId(clientId: Long):Future[Option[OAuthClient]] =
    db.run(oauthClients.filter(_.id===clientId).result.headOption)



}