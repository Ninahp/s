package com.africasTalking.elmer.food
package test

import akka.actor.ActorSystem

import akka.testkit.{ ImplicitSender, TestKit, TestProbe }

import org.scalatest.{ BeforeAndAfterAll, Matchers, WordSpecLike }

import io.atlabs._

import com.africasTalking._

import elmer.core.util.ElmerCoreServiceT

import BrokerService._

import FoodOrderService._

import elmer.food.marshalling._


abstract class ElmerFoodTestServiceT extends TestKit(ActorSystem("MyTestSystem"))
    with ImplicitSender
    with WordSpecLike
    with Matchers
    with BeforeAndAfterAll
    with ElmerCoreServiceT
{
  override def snoopServiceName = "TestService"
  override def actorRefFactory  = system

  val validOrder = FoodOrderServiceRequest(
    quantity = 2,
    name     = "Ugali"
  )

  val invalidOrder = FoodOrderServiceRequest(
    quantity = 2,
    name     = ""
  )
  override def beforeAll {
    Thread.sleep(1000)
  }

  override def afterAll {
    Thread.sleep(1000)
    TestKit.shutdownActorSystem(system)
  }

}
