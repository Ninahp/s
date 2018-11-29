package com.africasTalking.elmer
package worker.test

import akka.actor.ActorSystem
import akka.testkit.{ ImplicitSender, TestKit, TestProbe }
import org.scalatest.{ BeforeAndAfterAll, Matchers, WordSpecLike }

import io.atlabs._

import horus.core.snoop.ATSnoopService.PublishError

import com.africasTalking._

import elmer.core.util.ElmerCoreServiceT


abstract class WorkerTestService extends TestKit(ActorSystem("MyTestSystem"))
  with ImplicitSender
  with WordSpecLike
  with Matchers
  with BeforeAndAfterAll
  with ElmerCoreServiceT {
  override def snoopServiceName = "TestService"
  override def actorRefFactory  = system

  override def beforeAll {
    Thread.sleep(1000)
  }

  override def afterAll {
    Thread.sleep(1000)
    TestKit.shutdownActorSystem(system)
  }

  val snoopServiceProbe = TestProbe()
  def expectSnoopError(
                        message: String,
                        topic: Option[String] = None
                      ) = {
    var publishError = snoopServiceProbe.expectMsgType[PublishError]
    publishError.topic should be (topic)
    publishError.message.contains(message) should be (true)
  }

}
