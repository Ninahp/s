package com.africasTalking.elmer
package worker

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{ Success, Failure }

import akka.actor.{ Actor, ActorLogging, Props }
import akka.pattern.ask
import akka.util.Timeout

import io.atlabs._

import horus.core.util.ATCCPrinter
import horus.core.config.ATConfig

import com.africasTalking._

import elmer.core.util.ElmerEnum.FoodOrderStatus

import elmer.worker.gateway.FoodRequestGateway._
import elmer.worker.gateway.FoodRequestGateway


object FoodRequestService {
  case class FoodServiceRequest(name: String, quantity: Int) extends ATCCPrinter

  case class FoodServiceResponse(status: String,description:String) extends ATCCPrinter
}

class FoodRequestService extends Actor with ActorLogging {

  private val requestGateway  = createRequestGateway
  def createRequestGateway    = context.actorOf(Props[FoodRequestGateway])
  implicit val timeout        = Timeout(ATConfig.httpRequestTimeout)

  import FoodRequestService._

  override def receive: Receive = {
    case req: FoodServiceRequest =>
      log.info(s"Processing $req")

      val currentSender = sender()

        (requestGateway ? FoodGatewayRequest(req.name, req.quantity)).mapTo[FoodGatewayResponse] onComplete {
          case Success(res) =>
            currentSender ! FoodServiceResponse(res.status.toString, res.description)

          case Failure(ex)  =>
            currentSender ! FoodServiceResponse(FoodOrderStatus.InternalError.toString, "There was an internal error. Please try again")
        }
  }
}
