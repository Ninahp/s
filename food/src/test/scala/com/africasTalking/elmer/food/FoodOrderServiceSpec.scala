package com.africasTalking.elmer.food

import scala.concurrent.duration._

import akka.actor.ActorSystem
import akka.actor.Props
import akka.testkit.{ ImplicitSender, TestActors, TestKit }

import org.scalatest.{ BeforeAndAfterAll, Matchers, WordSpecLike }

import com.africasTalking._

import elmer.core.config._

import elmer.core.util.ElmerEnum.{ FoodName, OrderRequestStatus }

import elmer.test._

import FoodOrderService._

class FoodOrderServiceSpec extends ElmerFoodTestServiceT {
  "The FoodOrderService" must {
    "send the food order to the broker and return a FoodOrderServiceResponse" in {
      val foodOrderService 		  = system.actorOf(Props(new FoodOrderService))
      foodOrderService ! FoodOrderServiceRequest(
        name     = FoodName.Ugali,
        quantity = 2
      )
      expectMsg(FiniteDuration(20, "seconds"), FoodOrderServiceResponse(
        status       = OrderRequestStatus.Accepted,
        description  = "request Accepted"
      ))
    }
  }
}
