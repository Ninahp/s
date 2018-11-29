package com.africasTalking.elmer
package worker

import scala.util.{ Failure, Success }
import scala.concurrent.ExecutionContext.Implicits.global

import akka.actor.{ Actor, ActorLogging, Props }
import akka.pattern.ask
import akka.util.Timeout

import io.atlabs._

import horus.core.util.ATCCPrinter
import horus.core.config.ATConfig
import horus.core.snoop.SnoopErrorPublisherT

import com.africasTalking._

import elmer.core.util.ElmerEnum._

import gateway.FoodRequestGateway


object FoodRequestService {

  case class FoodServiceRequest(
     name: FoodEnum.Value,
     quantity: Int
   ) extends ATCCPrinter

  case class FoodServiceResponse(
    status: FoodOrderStatus.Value,
    description: String
  ) extends ATCCPrinter

}

class FoodRequestService extends Actor
  with ActorLogging
  with SnoopErrorPublisherT {

  private val requestGateway  = createRequestGateway
  def createRequestGateway    = context.actorOf(Props[FoodRequestGateway])
  implicit val timeout        = Timeout(ATConfig.httpRequestTimeout)

  import FoodRequestGateway._
  import FoodRequestService._

  override def receive: Receive = {
    case req: FoodServiceRequest =>

      log.info(s"Processing $req")

      val currentSender = sender()

        (requestGateway ? FoodGatewayRequest(req.name, req.quantity)).mapTo[FoodGatewayResponse] onComplete {
          case Success(res) =>
            currentSender ! FoodServiceResponse(
              res.status,
              res.description
            )

          case Failure(ex)  =>
            publishError(s"Error processing $req", Some(ex))

            currentSender ! FoodServiceResponse(
              FoodOrderStatus.InternalError,
              "There was an internal error. Please try again"
            )
        }
  }
}
