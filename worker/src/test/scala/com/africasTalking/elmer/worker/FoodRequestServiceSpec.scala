package com.africasTalking.elmer
package worker


import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit, TestProbe}

import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

import com.africasTalking.elmer._
import worker.FoodRequestGateway.IncomingFoodServiceResponse
import worker.FoodRequestService._


class FoodRequestServiceSpec extends TestKit(ActorSystem("MyTestSystem"))
  with ImplicitSender
  with WordSpecLike
  with Matchers
  with BeforeAndAfterAll {

  val foodRequestGatewayProbe = TestProbe()
  val gatewayService          = system.actorOf(Props[FoodRequestService])
  val validRequest            = FoodServiceRequest (name = "ugaLi", quantity = 1)
  val invalidRequest          = FoodServiceRequest (name = "Spaghetti", quantity = 4)

  "Food Request Service" must {
    "Accept valid requests and format it properly to send to the gateway" in {
      gatewayService ! validRequest
      expectMsg(IncomingFoodServiceResponse("Accepted","Request Accepted"))
    }

    "Reject invalid requests and return an informative response" in {
      gatewayService ! invalidRequest
      expectMsg(IncomingFoodServiceResponse("Bad Request", "Content was malformed"))
    }
  }

}
