package com.africasTalking.elmer.core
package query

import scala.concurrent.duration._

import akka.actor.Props
import akka.actor.ActorSystem
import akka.testkit.{ ImplicitSender, TestActors, TestKit }
import org.scalatest.{ BeforeAndAfterAll, Matchers, WordSpecLike }

import com.africasTalking._

import elmer.core.config._

import QueryService._

class QueryServiceSpec() extends TestKit(ActorSystem("QueryServiceSpec")) 
    with ImplicitSender
    with WordSpecLike 
    with Matchers 
    with BeforeAndAfterAll {

  override def afterAll: Unit = {
    TestKit.shutdownActorSystem(system)
  }

  "The QueryService" must {
    "fetch available food" in {
      val queryService 		  = system.actorOf(Props(new QueryService))
      queryService ! FoodFetchQueryServiceRequest
      expectMsgClass(FiniteDuration(20, "seconds"),classOf[List[String]])

    }

  }
}
