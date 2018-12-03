package com.africasTalking.elmer
package web

import scala.concurrent.duration._

import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.{RouteTestTimeout, ScalatestRouteTest}
import akka.util.ByteString

import org.scalatest.{ Matchers, WordSpec }

import com.africasTalking._

import elmer.core.util.ElmerCoreServiceT


class WebServiceSpec extends WordSpec
  with ScalatestRouteTest
  with Matchers
  with ElmerWebServiceT
  with ElmerCoreServiceT {

  def actorRefFactory = system

  implicit val routeTestTimeout = RouteTestTimeout(FiniteDuration(10, "seconds"))

  def testRequest(name: String, quantity: Int):HttpRequest = {
    val jsonRequest = ByteString(s"""{"name":"$name","quantity":$quantity}""".stripMargin)

    HttpRequest(
      HttpMethods.POST,
      uri = "/request",
      entity = HttpEntity(MediaTypes.`application/json`, jsonRequest))
  }

  "ElmerWebService" should {
    "return a Bad Request status when sent an invalid request" in{
      testRequest("Invalid",10) ~> Route.seal(route) ~> check {
        status shouldEqual StatusCodes.OK
        responseAs[String] shouldEqual """{"status":"BadRequest","description":"Error while sending request to the gateway"}"""
      }
    }

    "return an OK status when sent a valid request" in {
      testRequest("Ugali",10) ~> Route.seal(route) ~> check {
        status shouldEqual StatusCodes.OK
        responseAs[String] shouldEqual """{"status":"Accepted","description":"Request Accepted"}"""
      }
    }

    "return a MethodNotAllowed error for GET requests to a valid path" in {
      Get("/request") ~> Route.seal(route) ~> check {
        status shouldEqual StatusCodes.MethodNotAllowed
        responseAs[String] shouldEqual "HTTP method not allowed, supported methods: POST"
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
