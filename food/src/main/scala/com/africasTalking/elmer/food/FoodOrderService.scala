package com.africasTalking.elmer.food

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{ Success, Failure }

import akka.actor.{ Actor, ActorSystem, ActorLogging}
import akka.actor.Props
import akka.pattern.ask
import akka.util.Timeout

import spray.json._

import io.atlabs._

import horus.core.config.ATConfig
import horus.core.snoop.SnoopErrorPublisherT
import horus.core.util.ATCCPrinter

import com.africasTalking._

import elmer.core.util.ElmerEnum._

import elmer.food.gateway._


object FoodOrderService {
  case class PlaceOrder(
    order:FoodOrderGatewayRequest
  )extends ATCCPrinter

  case class FoodOrderGatewayRequest(
    name: String,
    quantity: Int
  )extends ATCCPrinter

  case class FoodOrderGatewayResponse(
    status: OrderRequestStatus.Value,
    description: String
  )extends ATCCPrinter

}

class FoodOrderService extends Actor
    with ActorLogging
    with SnoopErrorPublisherT {

  implicit val system           = context.system

  private val foodOrderGateway  = system.actorOf(Props[FoodOrderGateway])

  implicit val timeout          = Timeout(ATConfig.httpRequestTimeout)

  import FoodOrderGateway._
  import FoodOrderService._
  import context.dispatcher

  def receive: Receive = {
    case PlaceOrder(order) => 
      log.info("processing " + PlaceOrder)
      val currentSender = sender
      val response = (foodOrderGateway ? EtherOrderRequest(
              quantity = order.quantity,
              name     = order.name
    )).mapTo[EtherOrderResponse]

      response onComplete {
        case Success(response) =>
            currentSender ! FoodOrderGatewayResponse(
              status      = response.status,
              description = response.description
          )
        case Failure(error) =>
          publishError(s"Failure to retrieve response from broker $error")
            currentSender ! FoodOrderGatewayResponse(
              status      = OrderRequestStatus.Failure,
              description = "Failure to retrieve response from broker"
            ) 

      }
  }
}
