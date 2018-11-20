package com.africasTalking.elmer
package web

import scala.concurrent.duration._

import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.{RouteTestTimeout, ScalatestRouteTest}

import org.scalatest.{Matchers, WordSpec}


class WebServiceSpec extends WordSpec
  with ScalatestRouteTest
  with Matchers
  with ElmerWebServiceT {

  def actorRefFactory           = system
  implicit val routeTestTimeout = RouteTestTimeout(FiniteDuration(10, "seconds"))

  "ElmerWebService" should {
    "return a Bad Request status when sent an invalid request" in{
      Get("/request?name=spaghetti&quantity=10") ~> Route.seal(route) ~> check {
        responseAs[String] shouldEqual """{"status":"Bad Request","description":"Content was malformed"}"""
      }
    }

    "return an OK status when sent a valid request" in {
      Get("/request?name=ugali&quantity=2") ~> Route.seal(route) ~> check {
        responseAs[String] shouldEqual """{"status":"Accepted","description":"Request Accepted"}"""
      }
    }

    "return a MethodNotAllowed error for POST requests to a valid path" in {
      Post("/request") ~> Route.seal(route) ~> check {
        status shouldEqual StatusCodes.MethodNotAllowed
        responseAs[String] shouldEqual "HTTP method not allowed, supported methods: GET"
      }
    }

    "leave requests to base path unhandled" in {
      Get() ~> route ~> check {
        handled shouldEqual false
      }
    }

    "leave requests to other paths unhandled" in {
      Get("/other") ~> route ~> check {
        handled shouldEqual false
      }
    }
  }

}
