package com.africasTalking.elmer.food

import scala.concurrent.duration._

import akka.actor.Props
import akka.actor.ActorSystem
import akka.testkit.{ ImplicitSender, TestActors, TestKit }

import org.scalatest.{ BeforeAndAfterAll, Matchers, WordSpecLike }

import com.africasTalking._

import elmer.core.config._

import elmer.core.query.QueryService._

import FoodOrderService._

import elmer.food.marshalling._

import elmer.food.test._

class FoodOrderServiceSpec extends ElmerFoodTestServiceT {
  "The FoodOrderService" must {
    "send the food order to the broker and get a response" in {
      val foodOrderService 		  = system.actorOf(Props(new FoodOrderService))
      foodOrderService ! PlaceOrder(order)
      expectMsg(FiniteDuration(20, "seconds"), FoodOrderServiceResponse(
        status       = Some("Accepted"),
      ))
    }
  }
}
