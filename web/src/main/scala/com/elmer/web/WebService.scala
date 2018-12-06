package com.elmer
package web

import akka.actor.{ActorRefFactory, Props}
import akka.http.scaladsl.Http
import akka.io. {IO, Tcp}
import akka.util.Timeout
import akka.stream.ActorMaterializer

import com.elmer.brokerservice._
import core.config.FoodConfig
import RequestGateway.InboundResponse
import web.marshalling.WebJsonImplicitsT
import brokerservice.FoodRequestService
import brokerservice.FoodRequestSer


trait ElmerWebServiceT extends WebJsonImplicitsT {

  def actorRefFactory: ActorRefFactory
  implicit val timeout   = Timeout(FoodConfig.defaultTimeout)

  private val foodRequestService = createFoodService
  def createFoodService          = actorRefFactory.actorOf(Props[FoodRequestService])

  lazy val route = {
    path("request") {
      get{parameters('name.as[String], 'quantity.as[Int]) { (name, qty) =>
        complete{
          (foodRequestService ? FoodServiceRequest(name, qty)).mapTo[InboundFoodServiceResponse]
        }
      }}
    }
  }
}
