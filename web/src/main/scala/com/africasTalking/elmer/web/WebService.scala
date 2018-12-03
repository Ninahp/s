package com.africasTalking.elmer
package web

import akka.actor.{ ActorRefFactory, Props }
import akka.http.scaladsl.server.Directives._
import akka.pattern.ask
import akka.util.Timeout

import io.atlabs._

import horus.core.config.ATConfig

import com.africasTalking._

import elmer.core.util.ElmerCoreServiceT

import elmer.worker.FoodRequestService

import elmer.web.marshalling.WebJsonImplicitsT


trait ElmerWebServiceT extends WebJsonImplicitsT with ElmerCoreServiceT {

  def actorRefFactory: ActorRefFactory

  override def snoopServiceName = "elmer-web"
  implicit val timeout          = Timeout(ATConfig.httpRequestTimeout)

  private val foodRequestService = createFoodRequestService
  def createFoodRequestService          = actorRefFactory.actorOf(Props[FoodRequestService])

  import FoodRequestService._

  lazy val route = {
    path("request") {
      post { entity(as[FoodServiceRequest]) { request =>
          complete {
                (foodRequestService ? request).mapTo[FoodServiceResponse]
          }
        }
      }
    }
  }
}
