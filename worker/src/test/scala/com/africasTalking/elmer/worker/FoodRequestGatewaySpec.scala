package com.africasTalking.elmer
package worker

import akka.actor.Props

import com.africasTalking._

import elmer.core.util.ElmerEnum.Status

import elmer.worker.FoodRequestGateway.{ FoodGatewayRequest, FoodGatewayResponse }


class FoodRequestGatewaySpec extends FoodRequestBaseTestConfigT {

  val gatewayService  = system.actorOf(Props[FoodRequestGateway])

  val validRequest    = FoodGatewayRequest(name = "PepperSoup", quantity = 12)
  val invalidRequest  = FoodGatewayRequest(name = "Sauce", quantity = 0)

  val validResponse   = FoodGatewayResponse(Status.Accepted,"Request Accepted")
  val invalidResponse = FoodGatewayResponse(Status.BadRequest,"Content was malformed")

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
