package com.africasTalking.elmer.productservice

import scala.concurrent.duration._

import akka.actor.Props
import akka.actor.ActorSystem
import akka.testkit.{ ImplicitSender, TestActors, TestKit }

import org.scalatest.{ BeforeAndAfterAll, Matchers, WordSpecLike }

import com.africasTalking._

import elmer.core.config._

import elmer.core.query.QueryService._

import BrokerService._

class BrokerServiceSpec() extends TestKit(ActorSystem("BrokerServiceSpec")) with ImplicitSender
    with WordSpecLike 
    with Matchers 
    with BeforeAndAfterAll {

  override def afterAll: Unit = {
    TestKit.shutdownActorSystem(system)
  }
  val order = FoodOrderService(
    quantity = 2,
    name     = "Ugali"
  )
  "The BrokerService" must {
    "send the food order to the broker and get a response" in {
      val brokerService 		  = system.actorOf(Props(new BrokerService))
      brokerService ! PlaceOrder(order)
      expectMsgClass(FiniteDuration(20, "seconds"), classOf[FoodOrderServiceResponse])

    }

  }
}
