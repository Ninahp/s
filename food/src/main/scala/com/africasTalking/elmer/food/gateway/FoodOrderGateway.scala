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

import elmer.core.util.ElmerEnum._

import elmer.food.gateway.marshalling._ 


object FoodOrderGateway{
  case class EtherOrderRequest(
    quantity: Int,
    name: String
  )extends ATCCPrinter

  case class EtherOrderResponse(
    status: OrderRequestStatus.Value,
    description: String
  )extends ATCCPrinter

  case class EtherResponse(
    status: OrderRequestStatus.Value
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
    case req: EtherOrderRequest => 
      log.info("processing " + EtherOrderRequest)
      val currentSender = sender
      val requestFut  = for {
        entity   <- Marshal(req).to[MessageEntity]
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
          log.info(response.status.toString)
          response.status.isSuccess match {
            case true =>
              try {
                val etherResponse = response.data.parseJson.convertTo[EtherResponse]
                currentSender ! EtherOrderResponse(
                  status      = etherResponse.status,
                  description = "Successfully processed the response"
                )
              } catch {
                case ex: DeserializationException =>
                  publishError(s"Error while parsing json response [$response]", Some(ex))
              currentSender ! EtherOrderResponse(
                status          = OrderRequestStatus.Failure,
                description     = "Couldn't parse json response"
              )
                case ex: Throwable =>
                  publishError(s"Got an unexpected error on response [$response]", Some(ex))
              currentSender ! EtherOrderResponse(
                status          = OrderRequestStatus.Failure,
                description     = "Unexpected error"
              )            }
            case false =>
              publishError("Unexpected response " + response + " for request " + req)
              currentSender ! EtherOrderResponse(
              status          = OrderRequestStatus.Failure,
              description     = response.data
            )
          }
        case Failure(error) =>
          publishError(s"$error")
          currentSender ! EtherOrderResponse(
              status          = OrderRequestStatus.Failure,
              description     = "Failure getting response from gateway"
        )
      }
  }
}
