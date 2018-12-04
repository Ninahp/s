package com.africasTalking.elmer.gateway

import scala.concurrent.duration._

import akka.actor.Props
import akka.actor.ActorSystem
import akka.testkit.{ ImplicitSender, TestActors, TestKit }

import org.scalatest.{ BeforeAndAfterAll, Matchers, WordSpecLike }

import com.africasTalking._

import elmer.core.config._

import elmer.core.util.ElmerEnum._

import elmer.food.gateway._

import elmer.test._

import FoodOrderGateway._

class FoodOrderGatewaySpec extends ElmerFoodTestServiceT {
  val foodOrderGateway 		  = system.actorOf(Props(new FoodOrderGateway))
  "The FoodOrderGateway" must {
    "reject an order request lacking a valid food order name" in {
      foodOrderGateway ! EtherOrderRequest(
        name     = invalidOrder.name,
        quantity = invalidOrder.quantity
      )
      expectMsg(FiniteDuration(20, "seconds"), EtherOrderResponse(
        status       = OrderRequestStatus.Failure,
        description  = "The request content was malformed:\nObject is missing required member 'name'"
      ))
    }
  	"process a valid order request" in {
      foodOrderGateway ! EtherOrderRequest(
        name     = validOrder.name,
        quantity = validOrder.quantity
      )
      expectMsg(FiniteDuration(20, "seconds"), EtherOrderResponse(
        status       = OrderRequestStatus.Accepted,
       description   = "Successfully processed the response" 
      ))
    }
  }
}
