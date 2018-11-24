package com.africasTalking.elmer
package web

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.Directives._
import akka.pattern.ask
import akka.util.Timeout
import com.africasTalking.elmer.core.ElmerConfig
import com.africasTalking.elmer.worker.OrderService._
import com.africasTalking.elmer.worker.{OrderService, ProductMarshaller}
import io.atlabs.horus.core.util.ATUtil._
import scala.concurrent.duration._

import scala.concurrent.ExecutionContext


class RestApi(implicit  system : ActorSystem ) extends Routes{
  override implicit def executionContext: ExecutionContext = system.dispatcher

  override implicit def requestTimeOut: Timeout =  5 seconds //parseFiniteDuration(ElmerConfig.port).get

  override def createOrdererActor: ActorRef = system.actorOf(Props(new OrderService()),"orderer")

}


trait Routes extends ProductApi with ProductMarshaller {


  def routes = buy

  private def buy  = path("buy"){
    post {
      entity(as[ProductOrder]){ prod =>
        onSuccess(getProduct(prod))  {
          case Accepted => complete("Request Accepted")
          case Failure => complete("Request Failed")
          case Delivered => complete("Request Delivered")
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

  def getProduct(product : ProductOrder) = {
    (orderer ? product).mapTo[Response]
  }
}
