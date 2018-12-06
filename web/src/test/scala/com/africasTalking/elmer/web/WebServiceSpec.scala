package com.africasTalking.elmer.web

import scala.concurrent.duration._

import akka.actor.ActorRef
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model._
import akka.http.scaladsl.testkit.{ RouteTestTimeout, ScalatestRouteTest }
import akka.util.ByteString

import spray.json._
import DefaultJsonProtocol._

import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{ Matchers, WordSpec }

import com.africasTalking._

import elmer.food.FoodOrderService._
import elmer.web.marshalling._

class WebServiceSpec extends WordSpec 
  	with Matchers 
  	with ScalaFutures 
	  with ScalatestRouteTest
    with WebServiceT     
    with ElmerJsonSupportT {

  def actorRefFactory  = system

  lazy val routes = route

  implicit val routeTestTimeout = RouteTestTimeout(FiniteDuration(20, "seconds"))
  
  "ElmerWebService" should {
    "be able to add food orders (POST /food/order)" in {
      val order = JsObject(
        "name"     -> "Ugali".toJson,
        "quantity" -> 2.toJson
      )
      val request     = Post("/food/order", order)
      request ~> routes ~> check {
        status should ===(StatusCodes.OK)
        contentType should ===(ContentTypes.`application/json`)
      }
    }
  }

}