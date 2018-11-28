package com.africasTalking.elmer
package web

import scala.concurrent.ExecutionContext

import akka.actor.{ ActorRef, ActorSystem, Props}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.Directives._
import akka.pattern.ask
import akka.util.Timeout

import io.atlabs._

import horus.core._
import config.ATConfig

import core.util.ElmerEnum.Status._

import worker.OrderService._
import worker.{OrderService, ProductMarshaller}


class RestApi(implicit  system : ActorSystem ) extends Routes {
  override implicit def executionContext: ExecutionContext = system.dispatcher

  override implicit def requestTimeOut: Timeout = ATConfig.httpRequestTimeout

  override def createOrdererActor: ActorRef = system.actorOf(Props(new OrderService()),"Order-Service")

}


trait Routes extends ProductApi with ProductMarshaller {


  def routes = buy

  private def buy  = path("buy"){
    post {
      entity(as[OrderServiceRequest]){ prod =>
        onSuccess(getProduct(prod))  {
          case Accepted   =>  complete("Request Accepted")
          case Failure    =>  complete("Request Failed")
          case Delivered  =>  complete("Request Delivered")
        }

      }
    }
  }
}

trait ProductApi{

  implicit def executionContext : ExecutionContext

  implicit def requestTimeOut : Timeout

  def createOrdererActor : ActorRef

  lazy val orderer = createOrdererActor

  def getProduct(product : OrderServiceRequest) = {
    (orderer ? product).mapTo[Status]
  }
}
