package com.africasTalking.elmer
package worker

import akka.actor.{ActorRef, Props}
import akka.testkit.TestProbe

import com.africasTalking._

import elmer.core.util.ElmerEnum.FoodOrderStatus

import elmer.worker.FoodRequestService._
import elmer.worker.gateway.FoodRequestGateway._
import elmer.worker.test.WorkerTestService


class FoodRequestServiceSpec extends WorkerTestService {

  val foodRequestGatewayProbe = TestProbe()

  val foodRequestService          = system.actorOf(Props(new FoodRequestService {
    override def createRequestGateway: ActorRef = foodRequestGatewayProbe.ref
  }))

  val validRequest            = FoodServiceRequest (name = "Ugali", quantity = 1)
  val invalidRequest          = FoodServiceRequest (name = "Spaghetti", quantity = 1)

  "Food Request Service" must {

    "Return a Accepted if FoodName is valid" in {
      foodRequestService ! validRequest
      foodRequestGatewayProbe.expectMsg(FoodGatewayRequest("Ugali",1))
      foodRequestGatewayProbe.reply(FoodGatewayResponse(FoodOrderStatus.Accepted,"Request Accepted"))
      expectMsg(FoodServiceResponse(FoodOrderStatus.Accepted.toString,"Request Accepted"))
    }

    "Return a BadRequest if FoodName is invalid" in {
      foodRequestService ! invalidRequest
      foodRequestGatewayProbe.expectMsg(FoodGatewayRequest("Spaghetti",1))
      foodRequestGatewayProbe.reply(FoodGatewayResponse(FoodOrderStatus.BadRequest,"Content was malformed"))
      expectMsg(FoodServiceResponse(FoodOrderStatus.BadRequest.toString,"Content was malformed"))
    }
  }

}
