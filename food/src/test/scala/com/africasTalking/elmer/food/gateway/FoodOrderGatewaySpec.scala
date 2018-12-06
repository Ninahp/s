package com.africasTalking.elmer.gateway

import scala.concurrent.duration._

import akka.actor.Props
import akka.actor.ActorSystem
import akka.testkit.{ ImplicitSender, TestActors, TestKit }

import org.scalatest.{ BeforeAndAfterAll, Matchers, WordSpecLike }

import com.africasTalking._

import elmer.core.config._

import elmer.core.util.ElmerEnum.{ FoodName, OrderRequestStatus }

import elmer.food.gateway._

import elmer.test._

import FoodOrderGateway._

class FoodOrderGatewaySpec extends ElmerFoodTestServiceT {
  val foodOrderGateway 		  = system.actorOf(Props(new FoodOrderGateway))
  "The FoodOrderGateway" must {
    "process a valid order request and return a FoodOrderGatewayResponse" in {
      foodOrderGateway ! FoodOrderGatewayRequest(
        name     = FoodName.NaN,
        quantity = 3
      )
      expectMsg(FiniteDuration(20, "seconds"), FoodOrderGatewayResponse(
        status       = OrderRequestStatus.Failed,
        description  = "The request content was malformed:\nObject is missing required member 'name'"
      ))
    }
  	"process a valid order request" in {
      foodOrderGateway ! FoodOrderGatewayRequest(
        name     = FoodName.Ugali,
        quantity = 3
      )
      expectMsg(FiniteDuration(20, "seconds"), FoodOrderGatewayResponse(
        status       = OrderRequestStatus.Accepted,
       description   = "request Accepted" 
      ))
    }
  }
}
