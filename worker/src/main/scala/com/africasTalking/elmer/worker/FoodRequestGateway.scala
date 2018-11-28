package com.africasTalking.elmer
package worker.gateway

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{ Failure, Success }

import akka.actor.{ Actor, ActorLogging }
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model.{ HttpMethods, HttpRequest, MessageEntity }

import spray.json._

import io.atlabs._

import horus.core.http.client.ATHttpClientT
import horus.core.snoop.SnoopErrorPublisherT
import horus.core.util.ATCCPrinter

import com.africasTalking._

import elmer.core.config.ElmerConfig
import elmer.core.util.ElmerEnum.FoodOrderStatus

import elmer.worker.GatewayJsonSupportT
import elmer.worker._


object FoodRequestGateway {

  case class FoodGatewayRequest(name:String, quantity:Int) extends ATCCPrinter

  case class FoodGatewayResponse(status: FoodOrderStatus.Value, description: String) extends ATCCPrinter

}

class FoodRequestGateway extends Actor
  with ActorLogging
  with ATHttpClientT
  with GatewayJsonSupportT
  with SnoopErrorPublisherT {

  implicit val system = context.system
  val gatewayUrl      = ElmerConfig.gatewayUrl

  import FoodRequestGateway._

  override def receive: Receive = {
    case req: FoodGatewayRequest =>
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
              try {
                val res = response.data.parseJson.convertTo[GatewayResponse]
                res.status match {
                  case "Accepted" =>
                    currentSender ! FoodGatewayResponse(FoodOrderStatus.Accepted, "Request Accepted")

                  case "BadRequest" =>
                    currentSender ! FoodGatewayResponse(FoodOrderStatus.BadRequest, "Content was malformed")

                  case _ =>
                    currentSender ! FoodGatewayResponse(FoodOrderStatus.InternalError, "Internal Error. Please try again")
                }
              }
              catch{
                case ex:Throwable => publishError(s"Error in Parsing Json: $ex")
              }

            case false  =>
              log.warning(s"Processing $requestFut wasn't successful: ${response.status}")
              currentSender ! FoodGatewayResponse(FoodOrderStatus.BadRequest, "Error while sending request to the gateway")
          }

        case Failure(exception) =>
          publishError(s"Error in Sending request $requestFut: $exception")
          currentSender ! FoodGatewayResponse(FoodOrderStatus.InternalError, "There was an internal error. Please try again")
      }
  }
}
