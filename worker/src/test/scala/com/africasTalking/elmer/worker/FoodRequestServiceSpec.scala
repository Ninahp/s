package com.africasTalking.elmer
package worker

import scala.concurrent.duration._

import akka.actor.{ ActorRef, Props }
import akka.testkit.TestProbe

import com.africasTalking._

import elmer.core.util.ElmerEnum.{ FoodEnum, FoodOrderStatus }

import elmer.worker.gateway.FoodRequestGateway._
import elmer.worker.test.WorkerTestService


class FoodRequestServiceSpec extends WorkerTestService {

  val foodRequestGatewayProbe = TestProbe()

  import elmer.worker.FoodRequestService._

  val foodRequestService          = system.actorOf(Props(new FoodRequestService {
    override def createRequestGateway: ActorRef = foodRequestGatewayProbe.ref
  }))

  val validRequest            = FoodServiceRequest (name = FoodEnum.Ugali, quantity = 1)
  val invalidRequest          = FoodServiceRequest (name = FoodEnum.Invalid, quantity = 1)

  "Food Request Service" must {

    "Return a Accepted if FoodName is valid" in {
      foodRequestService ! validRequest
      foodRequestGatewayProbe.expectMsg(FoodGatewayRequest(FoodEnum.Ugali,1))
      foodRequestGatewayProbe.reply(FoodGatewayResponse(FoodOrderStatus.Accepted,"Request Accepted"))
      expectMsg(FoodServiceResponse(FoodOrderStatus.Accepted,"Request Accepted"))

      foodRequestGatewayProbe.expectNoMessage(100 millis)
    }

    "Return a BadRequest if FoodName is invalid" in {
      foodRequestService ! invalidRequest
      foodRequestGatewayProbe.expectMsg(FoodGatewayRequest(FoodEnum.Invalid,1))
      foodRequestGatewayProbe.reply(FoodGatewayResponse(FoodOrderStatus.BadRequest,"Content was malformed"))
      expectMsg(FoodServiceResponse(FoodOrderStatus.BadRequest,"Content was malformed"))

      foodRequestGatewayProbe.expectNoMessage(100 millis)
    }
  }

}
