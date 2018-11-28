package com.africasTalking.elmer
package web

import akka.actor.{ ActorRefFactory, Props }
import akka.http.scaladsl.server.Directives._
import akka.pattern.ask
import akka.util.Timeout

import io.atlabs._

import horus.core.config.ATConfig

import com.africasTalking._

import elmer.core.util.ElmerEnum._

import elmer.worker.FoodRequestService

import elmer.web.marshalling.WebJsonImplicitsT


trait ElmerWebServiceT extends WebJsonImplicitsT {

  import FoodRequestService._

  def actorRefFactory: ActorRefFactory
  implicit val timeout   = Timeout(ATConfig.httpRequestTimeout)

  private val foodRequestService = createFoodRequestService
  def createFoodRequestService          = actorRefFactory.actorOf(Props[FoodRequestService])

  lazy val route = {
    path("request") {
      post { entity(as[FoodServiceRequest]) {request =>
        complete{
          FoodEnum.contains(request.name) match {
            case true  =>
              (foodRequestService ? request).mapTo[FoodServiceResponse]

            case false =>
              FoodServiceResponse(FoodOrderStatus.BadRequest.toString, "Content was malformed")
          }
        }
      }}
    }
  }
}
