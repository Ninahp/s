package com.africasTalking.elmer
package worker

import scala.util.{ Failure, Success }
import scala.concurrent.ExecutionContext.Implicits.global

import akka.actor.{ Actor, ActorLogging }
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model.{ HttpMethods, HttpRequest, MessageEntity }

import io.atlabs._

import horus.core.http.client.ATHttpClientT
import horus.core.util.ATCCPrinter
import horus.core.snoop.SnoopErrorPublisherT

import com.africasTalking._

import elmer.core.util.ElmerEnum.Status
import elmer.core.config.ElmerConfig


object FoodRequestGateway {
  case class FoodGatewayRequest(name:String, quantity:Int) extends ATCCPrinter
  case class FoodGatewayResponse(status: Status.Value, description: String) extends ATCCPrinter
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
              //val res = response.data.parseJson.convertTo[GatewayResponse]
              currentSender ! FoodGatewayResponse(Status.Accepted, "Request Accepted")

            case false  =>
              log.warning(s"Processing $requestFut wasn't successful: ${response.status}")
              currentSender ! FoodGatewayResponse(Status.BadRequest, "Content was malformed")
          }

        case Failure(exception) =>
          publishError(s"Error in Sending request $requestFut: $exception")
          currentSender ! FoodGatewayResponse(Status.InternalError, "There was an internal error. Please try again")
      }
  }
}
