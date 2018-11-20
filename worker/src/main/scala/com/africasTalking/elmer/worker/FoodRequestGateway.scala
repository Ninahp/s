package com.africasTalking.elmer
package worker

import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global

import akka.actor.{Actor, ActorLogging}
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model.{HttpMethods, HttpRequest, MessageEntity}

import io.atlabs.horus.core.http.client.ATHttpClientT

import spray.json._

import com.africasTalking.elmer.core.config.ElmerConfig


object FoodRequestGateway {
  case class IncomingFoodServiceRequest(name:String,quantity:Int)
  case class IncomingFoodServiceResponse(status:String, description: String)
}

class FoodRequestGateway extends Actor
  with ActorLogging
  with ATHttpClientT
  with GatewayJsonSupportT {

  import FoodRequestGateway._

  implicit val system = context.system
  val gatewayUrl      = ElmerConfig.gatewayUrl

  override def receive: Receive = {
    case req: IncomingFoodServiceRequest =>
      val currentSender   = sender()

      log.info(s"Processing $req")
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

      requestFut onComplete{
        case Success(response) =>
          response.status.isSuccess match{
            case true   =>
              val res = response.data.parseJson.convertTo[GatewayResponse]
              currentSender ! IncomingFoodServiceResponse(res.status,"Request Accepted")

            case false  =>
              log.warning(s"Processing $requestFut wasn't successful: ${response.status}")
              currentSender ! IncomingFoodServiceResponse("Bad Request","Content was malformed")
          }

        case Failure(exception) =>
          log.error(s"Error in Sending request $requestFut: $exception")
      }
  }
}
