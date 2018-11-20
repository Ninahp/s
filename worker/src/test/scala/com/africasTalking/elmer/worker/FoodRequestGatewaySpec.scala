package com.africasTalking.elmer
package worker

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}

import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

import com.africasTalking.elmer.worker.FoodRequestGateway.{IncomingFoodServiceRequest, IncomingFoodServiceResponse}


class FoodRequestGatewaySpec extends TestKit(ActorSystem("MyTestSystem"))
  with ImplicitSender
  with WordSpecLike
  with Matchers
  with BeforeAndAfterAll {

  val gatewayService  = system.actorOf(Props[FoodRequestGateway])

  val validRequest    = IncomingFoodServiceRequest(name = "PepperSoup", quantity = 12)
  val invalidRequest  = IncomingFoodServiceRequest(name = "Sauce", quantity = 0)

  val validResponse   = IncomingFoodServiceResponse("Accepted","Request Accepted")
  val invalidResponse = IncomingFoodServiceResponse("Bad Request","Content was malformed")

  "Food Request Gateway" must {
    "Send and receive a response from the broker when sent a valid Request" in {
      gatewayService ! validRequest
      expectMsg(validResponse)
    }

    "Send and receive a failed response from the broker when sent an invalid Request" in {
      gatewayService ! invalidRequest
      expectMsg(invalidResponse)
    }
  }
}
