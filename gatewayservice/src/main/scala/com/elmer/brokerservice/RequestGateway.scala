package com.elmer
package gatewayservice

import spray.json._

import io.atlabs.horus.core.http.client.ATHttpClientT

import com.elmer.core.config.FoodConfig

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

import akka.actor.{Actor, ActorLogging}
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model._


object UserRequestGateway {
  case class InboundRequest(name:String,quantity:Int)
  case class InboundResponse(status:String, description: String, possibleVals: String)
}

class UserRequestGateway extends Actor
  with ActorLogging
  with ATHttpClientT
  with GatewayJsonSupportT {


  implicit val system  = context.system
  val gatewayUrl       = FoodConfig.gatewayUrl

  override def receive: Receive = {
    case req: InboundRequest =>
      val currentSender   = sender()

      logger.info(s"Processing $req")
      val requestFut = for {
        entity  <- Marshal(GatewayRequest(
          name     = req.name,
          quantity = req.quantity
        )).to[MessageEntity]
        response <- sendHttpRequest(
          HttpRequest(
            method = HttpMethods.POST,
            uri    = gatewayUrl,
            entity = entity
          )
        )
      } yield response

      ifSuccess onComplete{
        case Success(response) =>
          response.status.isSuccess match{
            case true   =>
              val res = response.data.parseJson.convertTo[GatewayResponse]
              currentSender ! InboundResponse(res.status,"Order Accepted!")

            case false  =>
              logger.warning(s"Processing $ifSuccess Failed: ${response.status}")
              currentSender ! InboundResponse("Bad Request","Error")
          }

        case Failure(exception) =>
          logger.error(s"Error in Sending request $ifSuccess: $exception")
      }
  }
}
