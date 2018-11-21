package com.africasTalking.elmer.web

import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.postfixOps
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

import com.africasTalking._

import elmer.core.config.ElmerConfig

import elmer.core.query.QueryService

import elmer.core.util._


import elmer.productservice._

import BrokerService._


trait WebServiceT extends ElmerJsonProtocol {

  implicit def actorRefFactory: ActorRefFactory

  private val queryService                  = actorRefFactory.actorOf(Props[QueryService])

  private val foodorderService              = actorRefFactory.actorOf(Props[BrokerService])

  implicit val timeout                      = Timeout(ElmerConfig.httpRequestTimeout)

  import QueryService._

  lazy val route = {
    path("food" / "order") {
        post {
          entity(as[FoodOrderService]) { order =>
                complete((foodorderService ? PlaceOrder(order)
            ).mapTo[FoodOrderServiceResponse])
          }
        }
    } ~
    path("food" / "fetch") {
        get {     
          onComplete((queryService ? FoodFetchQueryServiceRequest)) { 
            case Success(data) => 
              complete(data.asInstanceOf[List[String]])
            case Failure(e)    => 
              complete(StatusCodes.BadRequest)
        }
      }
    }
  }
}
