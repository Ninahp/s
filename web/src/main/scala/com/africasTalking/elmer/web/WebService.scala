package com.africasTalking.elmer
package web

import akka.actor.{ActorRefFactory, Props}
import akka.http.scaladsl.server.Directives._
import akka.pattern.ask
import akka.util.Timeout

import com.africasTalking.elmer.worker._
import core.config.ElmerConfig
import FoodRequestGateway.IncomingFoodServiceResponse
import web.marshalling.WebJsonImplicitsT
import worker.FoodRequestService
import worker.FoodRequestService._


trait ElmerWebServiceT extends WebJsonImplicitsT {

  def actorRefFactory: ActorRefFactory
  implicit val timeout   = Timeout(ElmerConfig.defaultTimeout)

  private val foodRequestService = createFoodService
  def createFoodService          = actorRefFactory.actorOf(Props[FoodRequestService])

  lazy val route = {
    path("request") {
      get{parameters('name.as[String], 'quantity.as[Int]) { (name, qty) =>
        complete{
          (foodRequestService ? FoodServiceRequest(name, qty)).mapTo[IncomingFoodServiceResponse]
        }
      }}
    }
  }
}
