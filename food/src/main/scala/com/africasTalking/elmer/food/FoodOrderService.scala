package com.africasTalking.elmer.food

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{ Success, Failure }

import akka.actor.{ Actor, ActorSystem, ActorLogging}
import akka.actor.Props
import akka.http.scaladsl.model._
import akka.pattern.ask
import akka.stream.ActorMaterializer
import akka.util.Timeout

import spray.json._

import io.atlabs._

import horus.core.config.ATConfig
import horus.core.util.ATCCPrinter

import com.africasTalking._

import elmer.core.config.ElmerConfig

import elmer.food.marshalling._

import BrokerService._


object FoodOrderService {
  case class PlaceOrder(
    order:FoodOrderServiceRequest
  )extends ATCCPrinter

  case class PlaceOrderRequest(
    order:FoodOrderServiceRequest
  )extends ATCCPrinter
}

class FoodOrderService extends Actor
    with ActorLogging
    with ElmerJsonSupportT {

  implicit val system                       = context.system

  private val brokerService                 = system.actorOf(Props[BrokerService])

  implicit val timeout                      = Timeout(ATConfig.httpRequestTimeout)

  import FoodOrderService._
  import context.dispatcher

  def receive: Receive = {
    case PlaceOrder(order) => 
      log.info("processing " + PlaceOrder)
      val currentSender = sender
      val response = (brokerService ? PlaceOrderRequest(order))

      response onComplete {
        case Success(response) =>
            try {
              val brokerResponse = response.asInstanceOf[FoodOrderServiceResponse]
              currentSender ! brokerResponse
            } catch {
              case ex: Throwable =>
                log.info(s"Error while processing broker response [$response] for request [$order]", Some(ex))
            currentSender ! FoodOrderServiceResponse(
              status          = None
            )                }
        case Failure(error) =>
          log.info(s"$error")
            currentSender ! FoodOrderServiceResponse(
              status          = None
            ) 
      }
  }
}
