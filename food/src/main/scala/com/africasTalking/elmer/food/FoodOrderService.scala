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

import elmer.core.util.ElmerEnum.{ FoodName, OrderRequestStatus }

import elmer.food.gateway._


object FoodOrderService {
  case class FoodOrderServiceRequest(
    name: FoodName.Value,
    quantity: Int
  )extends ATCCPrinter

  case class FoodOrderServiceResponse(
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
    case order: FoodOrderServiceRequest => 
      log.info("processing " + FoodOrderServiceRequest)
      val currentSender = sender
      val response = (foodOrderGateway ? FoodOrderGatewayRequest(
              quantity = order.quantity,
              name     = order.name
    )).mapTo[FoodOrderGatewayResponse]

      response onComplete {
        case Success(response) =>
            currentSender ! FoodOrderServiceResponse(
              status      = response.status,
              description = response.description
          )
        case Failure(error) =>
          publishError(s"Failed to retrieve response from broker for [$order]", Some(error))
            currentSender ! FoodOrderServiceResponse(
              status      = OrderRequestStatus.Failed,
              description = "Error while processing request"
            ) 

      }
  }
}
