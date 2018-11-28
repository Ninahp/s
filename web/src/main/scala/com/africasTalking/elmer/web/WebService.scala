package com.africasTalking.elmer.web

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{ Failure, Success }

import akka.actor.ActorRefFactory
import akka.actor.Props
import akka.event.Logging
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers._
import akka.http.scaladsl.server.Directives._
import akka.pattern.ask
import akka.util.Timeout
import akka.event.Logging

import io.atlabs._

import horus.core.config.ATConfig

import com.africasTalking._

import elmer.core.config.ElmerConfig

import elmer.core.util.ElmerCoreServiceT

import elmer.food._

import FoodOrderService._

import elmer.web.marshalling._


trait WebServiceT extends ElmerJsonSupportT
 with ElmerCoreServiceT{
  
  override def snoopServiceName = "ElmerSnoopService"
  
  private val foodOrderService  = actorRefFactory.actorOf(Props[FoodOrderService])

  implicit val timeout          = Timeout(ATConfig.httpRequestTimeout)

  import BrokerService._


  lazy val route = {
    path("food" / "order") {
        post {
          entity(as[FoodOrderServiceRequest]) { order =>
                complete((foodOrderService ? PlaceOrder(order)
            ).mapTo[FoodOrderServiceResponse])
          }
        }
      }
  }
}
