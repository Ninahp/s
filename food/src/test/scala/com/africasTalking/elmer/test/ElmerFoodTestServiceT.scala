package com.africasTalking.elmer.test

import akka.actor.ActorSystem

import akka.testkit.{ ImplicitSender, TestKit, TestProbe }

import org.scalatest.{ BeforeAndAfterAll, Matchers, WordSpecLike }

import io.atlabs._

import com.africasTalking._

import elmer.core.util.ElmerCoreServiceT

import elmer.core.util.ElmerJsonProtocol._

import elmer.food._

import FoodOrderService._


abstract class ElmerFoodTestServiceT extends TestKit(ActorSystem("MyTestSystem"))
    with ImplicitSender
    with WordSpecLike
    with Matchers
    with BeforeAndAfterAll
    with ElmerCoreServiceT
{
  override def snoopServiceName = "TestService"
  override def actorRefFactory  = system

  override def beforeAll {
    Thread.sleep(1000)
  }

  override def afterAll {
    Thread.sleep(1000)
    TestKit.shutdownActorSystem(system)
  }

}
