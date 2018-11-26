package com.africasTalking.elmer.food

import scala.concurrent.duration._

import akka.actor.Props
import akka.actor.ActorSystem
import akka.testkit.{ ImplicitSender, TestActors, TestKit }

import org.scalatest.{ BeforeAndAfterAll, Matchers, WordSpecLike }

import com.africasTalking._

import elmer.core.config._

import elmer.food.marshalling._

import elmer.food.test._

import BrokerService._

import FoodOrderService._

class BrokerServiceSpec extends ElmerFoodTestServiceT {
  val brokerService 		  = system.actorOf(Props(new BrokerService))
  "The BrokerService" must {
    "reject an invalid order request" in {
      brokerService ! PlaceOrderRequest(invalidOrder)
      expectMsg(FiniteDuration(20, "seconds"), FoodOrderServiceResponse(
        status       = None
      ))
    }
  	"process a valid order request" in {
      brokerService ! PlaceOrderRequest(validOrder)
      expectMsg(FiniteDuration(20, "seconds"), FoodOrderServiceResponse(
        status       = Some("Accepted")
      ))
    }
  }
}
