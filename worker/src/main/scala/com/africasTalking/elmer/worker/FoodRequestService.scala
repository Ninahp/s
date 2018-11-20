package com.africasTalking.elmer
package worker

import scala.concurrent.ExecutionContext.Implicits.global

import akka.actor.{Actor, ActorLogging, Props}
import akka.pattern.ask
import akka.pattern.pipe
import akka.util.Timeout

import com.africasTalking.elmer.core._
import config.ElmerConfig
import util.StringComplianceServiceT
import worker.FoodRequestGateway._


object FoodRequestService {
  case class FoodServiceRequest(name: String, quantity: Int)
  case class FoodServiceResponse(status: String,description:String)
}

class FoodRequestService extends Actor
  with ActorLogging
  with StringComplianceServiceT {

  import FoodRequestService._

  val lunchRequestGateway     = createRequestGateway
    def createRequestGateway  = context.actorOf(Props[FoodRequestGateway])
  implicit val timeout        = Timeout(ElmerConfig.defaultTimeout)

  override def receive: Receive = {
    case req: FoodServiceRequest =>
      log.info(s"Processing $req")

      val currentSender = sender()
      val nameFormat    = checkString(req.name)

      nameFormat =="" match{
        case true  =>
          //The word doesn't exist
          currentSender ! IncomingFoodServiceResponse("Bad Request", "Content was malformed")

        case false =>
          (lunchRequestGateway ? IncomingFoodServiceRequest(nameFormat, req.quantity)).mapTo[IncomingFoodServiceResponse] pipeTo currentSender
      }
  }
}
