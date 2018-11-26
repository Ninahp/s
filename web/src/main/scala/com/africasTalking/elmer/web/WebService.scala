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

import elmer.core.query.QueryService

import elmer.food._

import FoodOrderService._

import elmer.web.marshalling._


trait WebServiceT extends ElmerJsonSupportT {

  implicit def actorRefFactory: ActorRefFactory

  private val queryService                  = actorRefFactory.actorOf(Props[QueryService])

  private val FoodOrderService              = actorRefFactory.actorOf(Props[FoodOrderService])

  implicit val timeout                      = Timeout(ATConfig.httpRequestTimeout)

  import QueryService._

  lazy val route = {
    path("food" / "order") {
        post {
          entity(as[FoodOrderServiceRequest]) { order =>
                complete((FoodOrderService ? PlaceOrder(order)
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
