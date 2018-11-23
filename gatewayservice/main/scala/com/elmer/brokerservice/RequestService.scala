package com.elmer
package worker

import scala.concurrent.ExecutionContext.Implicits.global

import akka.actor.{Actor, ActorLogging, Props}
import akka.pattern.ask
import akka.pattern.pipe
import akka.util.Timeout

import com.elmer.core._
import config.FoodConfig
import util.TestStringT
import brokerService.RequestGateway._


object OrderService {
  case class OderRequest(name: String, quantity: Int)
  case class OderResponse(status: String,description:String, possibleVals: String)
}

class OrderService extends Actor
  with ActorLogging
  with TestStringT {

  import OrderService._

  val someRequest             = createRequestGateway
  def createRequestGateway    = context.actorOf(Props[RequestGateway])
  implicit val timeout        = Timeout(FoodConfig.defaultTimeout)

  override def receive: Receive = {
    case req: OrderRequest =>
      logger.info(s"Processing $req")

      val currentSender = sender()
      val nameFormat    = checkStr(req.name)

      nameFormat =="" match{
        case true  =>
          currentSender ! InboundResponse("Bad Request", "Error")

        case false =>
          (someRequest ? InboundRequest(nameFormat, req.quantity)).mapTo[InboundResponse] pipeTo currentSender
      }
  }
}
