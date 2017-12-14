package utils

import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.model.headers._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Directive0, Route}
import com.typesafe.config.ConfigFactory

trait CorsSupport {

  private def addAccessControlHeader: Directive0 = {

    mapResponseHeaders {
      val corsConfig = ConfigFactory.load().getConfig("cors")
      x =>
        `Access-Control-Allow-Origin`.* +:
          `Access-Control-Allow-Credentials`(true) +:
          `Access-Control-Allow-Headers`(corsConfig.getString("origin")) +:
          `Access-Control-Max-Age`(corsConfig.getInt("maxAge")) +:
          x
    }
  }

  private def preflightRequestHandler: Route = options {
    complete(HttpResponse(200).withHeaders(
      `Access-Control-Allow-Methods`(OPTIONS, POST, PUT, GET, DELETE)
    ))
  }

  def corsHandler(r: Route) = addAccessControlHeader {
    preflightRequestHandler ~ r
  }

}
