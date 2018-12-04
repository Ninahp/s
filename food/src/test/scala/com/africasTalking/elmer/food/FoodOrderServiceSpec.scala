package com.africasTalking.elmer.food

import scala.concurrent.duration._

import akka.actor.ActorSystem
import akka.actor.Props
import akka.testkit.{ ImplicitSender, TestActors, TestKit }

import org.scalatest.{ BeforeAndAfterAll, Matchers, WordSpecLike }

import com.africasTalking._

import elmer.core.config._

import elmer.core.util.ElmerEnum._

import elmer.test._

import FoodOrderService._

class FoodOrderServiceSpec extends ElmerFoodTestServiceT {
  "The FoodOrderService" must {
    "send the food order to the broker and get a response" in {
      val foodOrderService 		  = system.actorOf(Props(new FoodOrderService))
      foodOrderService ! PlaceOrder(validOrder)
      expectMsg(FiniteDuration(20, "seconds"), FoodOrderGatewayResponse(
        status       = OrderRequestStatus.Accepted,
        description  = "Successfully processed the response"
      ))
    }
  }
}
