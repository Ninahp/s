package com.africasTalking.elmer
package worker.test

import akka.actor.ActorSystem
import akka.testkit.{ ImplicitSender, TestKit }

import org.scalatest.{ BeforeAndAfterAll, Matchers, WordSpecLike }


class WorkerTestService extends TestKit(ActorSystem("MyTestSystem"))
  with ImplicitSender
  with WordSpecLike
  with Matchers
  with BeforeAndAfterAll
