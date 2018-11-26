package com.africasTalking.elmer.food

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{ Success, Failure }

import akka.actor.{ Actor, ActorSystem, ActorLogging }
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model._
import akka.pattern.ask
import akka.stream.ActorMaterializer
import akka.util.ByteString
import akka.util.Timeout

import spray.json._

import io.atlabs._

import horus.core.http.client.ATHttpClientT

import horus.core.util.{ ATJsonProtocol, ATCCPrinter, ATUtil }

import com.africasTalking._

import elmer.core.config.ElmerConfig

import elmer.food.marshalling._ 

import FoodOrderService._ 

object BrokerService{
  case class FoodOrderServiceRequest(
    quantity: Int,
    name: String
  )extends ATCCPrinter
// I would prefer is status was an Enum with values such as Accepted,
 // Completed, Failed (matching what either publishes)
  case class FoodOrderServiceResponse(
    status: Option[String],
  )extends ATCCPrinter

}

class BrokerService extends Actor
    with ActorLogging
    with ATHttpClientT
    with ElmerJsonSupportT {

  implicit val system = context.system

  val url             = ElmerConfig.brokerUrl

  import BrokerService._
  import FoodOrderService._
  import context.dispatcher

  def receive: Receive = {
    case PlaceOrderRequest(order) => 
      log.info("processing " + PlaceOrderRequest)
      val currentSender = sender
      val requestFut  = for {
        entity   <- Marshal(order).to[MessageEntity]
        response <- sendHttpRequest(
          HttpRequest(
            method = HttpMethods.POST,
            uri    = url,
            entity = entity
          )
        )
      } yield response

      requestFut onComplete {
        case Success(response) =>
          log.info(response.status.toString)
          response.status.isSuccess match {
            case true =>
              currentSender ! response.data.parseJson.convertTo[FoodOrderServiceResponse]
            case false =>
              // Extend horus.core.snoop.SnoopErrorPublisherT and publish an error here
              log.info("Unexpected response " + response + " for request " + order)
              currentSender ! FoodOrderServiceResponse(
              status          = None
            )
          }
          // Publish an error here and send a response back to the currentSender
        case Failure(error) =>
          log.info(s"$error")
      }
  }
}
// Wrap this in a try-catch block, in case we run into any JSON errors.
// Also, I would rather you first read the response 
// into the response type then compose a FoodOrderServiceResponse
