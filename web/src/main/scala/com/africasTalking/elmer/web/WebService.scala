package com.africasTalking.elmer
package web

import akka.actor.{ ActorRefFactory, Props }
import akka.http.scaladsl.server.Directives._
import akka.pattern.ask
import akka.util.Timeout

import com.africasTalking._

import elmer.core.config.ElmerConfig

import elmer.worker.FoodRequestGateway.FoodGatewayResponse
import elmer.worker.FoodRequestService

import elmer.web.marshalling.WebJsonImplicitsT


trait ElmerWebServiceT extends WebJsonImplicitsT {

  import FoodRequestService._

  def actorRefFactory: ActorRefFactory
  implicit val timeout   = Timeout(ElmerConfig.defaultTimeout)

  private val foodRequestService = createFoodRequestService
  def createFoodRequestService          = actorRefFactory.actorOf(Props[FoodRequestService])

  lazy val route = {
    path("request") {
      post { entity(as[FoodServiceRequest]) {request =>
        complete{
          (foodRequestService ? request).mapTo[FoodGatewayResponse]
        }
      }}
    }
  }
}
