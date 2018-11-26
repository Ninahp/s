package com.africasTalking.elmer
package worker

import scala.concurrent.duration._

import akka.actor.{ActorRef, Props}
import akka.testkit.TestProbe

import com.africasTalking._

import elmer.core.util.ElmerEnum.Status

import elmer.worker.FoodRequestGateway._
import elmer.worker.FoodRequestService._


class FoodRequestServiceSpec extends FoodRequestBaseTestConfigT {

  val foodRequestGatewayProbe = TestProbe()

  val foodRequestService          = system.actorOf(Props(new FoodRequestService {
    override def createRequestGateway: ActorRef = foodRequestGatewayProbe.ref
  }))

  val validRequest            = FoodServiceRequest (name = "ugaLi", quantity = 1)
  val invalidRequest          = FoodServiceRequest (name = "Spaghetti", quantity = 4)

  "Food Request Service" must {
    "Return a BadRequest if FoodName is invalid" in {
      foodRequestService ! invalidRequest
      expectMsg(FoodGatewayResponse(Status.BadRequest, "Content was malformed"))
      foodRequestGatewayProbe.expectNoMessage(100 millis)
      expectNoMessage(100 millis)
    }

    "Return a Accepted if FoodName is valid" in {
      foodRequestService ! validRequest
      foodRequestGatewayProbe.expectMsg(FoodGatewayRequest("Ugali",1))
      foodRequestGatewayProbe.reply(FoodGatewayResponse(Status.Accepted,"Request Accepted"))
      expectMsg(FoodGatewayResponse(Status.Accepted,"Request Accepted"))
    }
  }

}
