package com.africasTalking.elmer.web

import scala.util.{ Failure, Success }

import akka.actor.ActorRefFactory
import akka.actor.Props
import akka.http.scaladsl.server.Directives._
import akka.pattern.ask
import akka.util.Timeout

import io.atlabs._

import horus.core.config.ATConfig

import com.africasTalking._

import elmer.core.util.ElmerCoreServiceT

import elmer.food._

import elmer.web.marshalling._

import FoodOrderService._


trait WebServiceT extends ElmerJsonSupportT
 with ElmerCoreServiceT{
  
  override def snoopServiceName = "ElmerSnoopService"
  
  private val foodOrderService  = actorRefFactory.actorOf(Props[FoodOrderService])

  implicit val timeout          = Timeout(ATConfig.httpRequestTimeout)

  lazy val route = {
    path("food" / "order") {
        post {
          entity(as[FoodOrderServiceRequest]) { order =>
                complete((foodOrderService ? order
            ).mapTo[FoodOrderServiceResponse])
          }
        }
      }
  }
}
