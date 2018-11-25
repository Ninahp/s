package com.africasTalking.elmer.food

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{ Success, Failure }

import akka.actor.{ Actor, ActorSystem, ActorLogging}
import akka.pattern.ask
import akka.util.Timeout
import akka.stream.ActorMaterializer
import akka.http.scaladsl.model._
import akka.http.scaladsl.marshalling.Marshal
import akka.util.ByteString

import spray.json._

import io.atlabs._

import horus.core.util.{ ATJsonProtocol, ATUtil }
import horus.core.http.client.ATHttpClientT

import com.africasTalking._

import elmer.core.config.ElmerConfig

import elmer.core.query.QueryService._

import elmer.food.marshalling._

import FoodOrderService._ 

class BrokerService extends Actor
    with ActorLogging
    with ATHttpClientT
    with ElmerJsonSupportT {

  implicit val system = context.system

  val url             = ElmerConfig.brokerUrl

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
              log.info("Unexpected response " + response + " for request " + order)
              currentSender ! FoodOrderServiceResponse(
              status          = None
            )
          }
        case Failure(error) =>
          log.info(s"$error")
      }
  }
}
