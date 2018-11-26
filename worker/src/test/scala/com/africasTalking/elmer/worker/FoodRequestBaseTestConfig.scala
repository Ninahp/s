package com.africasTalking.elmer
package worker

import akka.actor.ActorSystem
import akka.testkit.{ ImplicitSender, TestKit }

import org.scalatest.{ BeforeAndAfterAll, Matchers, WordSpecLike }


trait FoodRequestBaseTestConfigT extends  FoodRequestBaseTestConfig

class FoodRequestBaseTestConfig extends TestKit(ActorSystem("MyTestSystem"))
  with ImplicitSender
  with WordSpecLike
  with Matchers
  with BeforeAndAfterAll
