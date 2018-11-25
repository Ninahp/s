package com.elmer
package gatewayservice

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}

import org.scalatest.

import com.elmer.gatewayservice.RequestGateway.{InboundRequest, InboundResponse}


class RequestGatewaySpec extends TestKit(ActorSystem(""))
  with ImplicitSender
{

  val gatewayService  = system.actorOf(Props[RequestGateway])

  val correctOrderRequest    = InboundRequest(name = "Pilau", quantity = 1)
  val wrongOrderRequest      = InboundRequest(name = "Salad", quantity = 0)

  val correctOrderResponse   = InboundResponse("Accepted","Order Request Was Accepted")
  val wrongOrderResponse     = InboundResponse("Rejected","Incorrect Order Request")

  it {
    ("should send and receive a validated response from the broker service if the correct order request is given") {
      gatewayService ! correctOrderRequest
      expectMsg(correctOrderRequest)
    }

   it (" Should Send and receive a failed response from the brokerif the wrong order request is given") {
      gatewayService ! wrongOrderRequest
      expectMsg(wrongOrderRequest)
    }
  }
}
