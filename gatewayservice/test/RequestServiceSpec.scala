package com.elmer
package gatewayservice


import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}

import org.scalatest

import com.elmer._
import gatewayservice.RequestGateway.InboundResponse
import gatewayservice.RequestService._


class RequestServiceSpec extends TestKit(ActorSystem(""))
  with ImplicitSender
  {

  val gatewayService         = system.actorOf(Props[GatewayService])
  val correctOrderRequest    = OderRequest (name = "Pilau", quantity = 1)
  val wrongOrderResponse     = OrderRequest (name = "SaLad", quantity = 1)

  it {
    ("Should accept correct order requests") {
      gatewayService ! correctOrderRequest
      expectMsg(InboundResponse("Accepted","Order Request Was Accepted"))
    }

    it ("Should reject incorrect order request") {
      gatewayService ! wrongOrderResponse
      expectMsg(InboundResponse("Rejected","Incorrect Order Request"))
    }
  }

}
