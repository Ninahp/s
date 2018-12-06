package com.africasTalking.elmer.food
package gateway

import scala.util.{ Success, Failure }

import akka.actor.{ Actor, ActorLogging }
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model._

import spray.json._

import io.atlabs._

import horus.core.http.client.ATHttpClientT
import horus.core.snoop.SnoopErrorPublisherT
import horus.core.util.ATCCPrinter

import com.africasTalking._

import elmer.core.config.ElmerConfig
import elmer.core.util.ElmerEnum.{ FoodName, OrderRequestStatus }

import marshalling._


object FoodOrderGateway{
  case class FoodOrderGatewayRequest(
    name: FoodName.Value,
    quantity: Int
  )extends ATCCPrinter

  case class FoodOrderGatewayResponse(
    status: OrderRequestStatus.Value,
    description: String
  )extends ATCCPrinter
}

class FoodOrderGateway extends Actor
    with ActorLogging
    with ATHttpClientT
    with ElmerJsonSupportT 
    with SnoopErrorPublisherT {

  implicit val system = context.system

  val gatewayUrl      = ElmerConfig.brokerOrderRequestUrl

  import FoodOrderGateway._
  import context.dispatcher

  def receive: Receive = {
    case req: FoodOrderGatewayRequest => 
      log.info("processing " + EtherOrderRequest)
      val currentSender = sender
      val etherOrderRequest = EtherOrderRequest(
        name      = req.name,
        quantity  = req.quantity
      )
      val requestFut  = for {
        entity   <- Marshal(etherOrderRequest).to[MessageEntity]
        response <- sendHttpRequest(
          HttpRequest(
            method = HttpMethods.POST,
            uri    = gatewayUrl,
            entity = entity
          )
        )
      } yield response

      requestFut onComplete {
        case Success(response) =>
          response.status.isSuccess match {
            case true =>
              try {
                val etherResponse = response.data.parseJson.convertTo[EtherOrderResponse]
                currentSender ! FoodOrderGatewayResponse(
                  status      = etherResponse.status,
                  description = "request " + etherResponse.status
                )
              } catch {
                case ex: DeserializationException =>
                  publishError(s"Error while parsing json response [$response]", Some(ex))
                    currentSender ! FoodOrderGatewayResponse(
                      status          = OrderRequestStatus.Failed,
                      description     = "Service couldn't process the response"
                    )
                case ex: Throwable =>
                  publishError(s"Got an unexpected error on response [$response]", Some(ex))
                    currentSender ! FoodOrderGatewayResponse(
                      status          = OrderRequestStatus.Failed,
                      description     = "Unexpected error"
                    )            }
            case false =>
              publishError("Unexpected response " + response + " for request " + req)
                currentSender ! FoodOrderGatewayResponse(
                status          = OrderRequestStatus.Failed,
                description     = response.data
              )
          }
        case Failure(error) =>
          publishError(s"$error")
            currentSender ! FoodOrderGatewayResponse(
                status          = OrderRequestStatus.Failed,
                description     = "Failed getting response from gateway"
          )
      }
  }
}
