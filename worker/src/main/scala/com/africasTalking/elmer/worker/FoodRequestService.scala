package com.africasTalking.elmer
package worker

import scala.concurrent.ExecutionContext.Implicits.global

import akka.actor.{ Actor, ActorLogging, Props }
import akka.pattern.{ ask, pipe }
import akka.util.Timeout

import io.atlabs._

import horus.core.util.ATCCPrinter

import com.africasTalking._

import elmer.core.util.ElmerEnum.Status
import elmer.core.config.ElmerConfig
import elmer.core.util.StringComplianceServiceT
import elmer.worker.FoodRequestGateway._


object FoodRequestService {
  case class FoodServiceRequest(name: String, quantity: Int) extends ATCCPrinter
  case class FoodServiceResponse(status: String,description:String) extends ATCCPrinter
}

class FoodRequestService extends Actor
  with ActorLogging
  with StringComplianceServiceT {

  import FoodRequestService._

  private val requestGateway  = createRequestGateway
  def createRequestGateway    = context.actorOf(Props[FoodRequestGateway])
  implicit val timeout        = Timeout(ElmerConfig.defaultTimeout)

  override def receive: Receive = {
    case req: FoodServiceRequest =>
      log.info(s"Processing $req")

      val currentSender = sender()
      val nameFormat    = checkString(req.name)

      nameFormat =="" match{
        case true  =>
          //The word doesn't exist
          currentSender ! FoodGatewayResponse(Status.BadRequest, "Content was malformed")

        case false =>
          (requestGateway ? FoodGatewayRequest(nameFormat, req.quantity)).mapTo[FoodGatewayResponse] pipeTo currentSender
      }
  }
}
