package com.africasTalking.elmer.food

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{ Success, Failure }

import akka.actor.{ Actor, ActorSystem, ActorLogging, ActorRef }
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model._
import akka.pattern.ask
import akka.stream.ActorMaterializer
import akka.util.ByteString
import akka.util.Timeout

import spray.json._

import io.atlabs._

import horus.core.http.client.ATHttpClientT
import horus.core.snoop.SnoopErrorPublisherT
import horus.core.util.{ ATJsonProtocol, ATCCPrinter, ATInstanceManagerT, ATUtil }

import com.africasTalking._

import elmer.core.config.ElmerConfig

import elmer.core.util.ElmerEnum._

import elmer.food.marshalling._ 

import FoodOrderService._ 

object BrokerService{
  case class FoodOrderServiceRequest(
    quantity: Int,
    name: String
  )extends ATCCPrinter

  case class FoodOrderServiceResponse(
    status: Status.Value,
  )extends ATCCPrinter

}

class BrokerService extends Actor
    with ActorLogging
    with ATHttpClientT
    with ElmerJsonSupportT 
    with SnoopErrorPublisherT {

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
              try {
                val brokerResponse = response.data.parseJson.convertTo[FoodOrderServiceResponse]
                currentSender ! brokerResponse
              } catch {
                case ex: Throwable =>
                  publishError(s"Error while parsing json response [$response]", Some(ex))
              currentSender ! FoodOrderServiceResponse(
                status          = Status.Failure
              )                }
            case false =>
              currentSender ! FoodOrderServiceResponse(
              status          = Status.Failure
            )
              publishError("Unexpected response " + response + " for request " + order)
          }
        case Failure(error) =>
          publishError(s"$error")
          currentSender ! FoodOrderServiceResponse(
              status          = Status.Failure
        )
      }
  }
}
