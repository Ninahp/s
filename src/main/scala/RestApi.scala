import Orderer.ProductOrder
import akka.actor.{ActorRef, ActorSystem, Props}
import akka.util.Timeout

import scala.concurrent.ExecutionContext
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json.DefaultJsonProtocol._


class RestApi(implicit  system : ActorSystem , timeout: Timeout) extends Routes{
  override implicit def executionContext: ExecutionContext = system.dispatcher

  override implicit def requestTimeOut: Timeout = timeout

  override def createOrdererActor: ActorRef = system.actorOf(Props(new Orderer()))

}


trait Routes extends ProductApi with ProductMarshaller {
  import Orderer._

  def routes = buy

  private def buy  = path("buy"){
    post {
      entity(as[ProductOrder]){ prod =>
        getProduct(prod)
        complete("Thanks")
      }
    }
  }




}

trait ProductApi{

import Orderer._
  implicit def executionContext : ExecutionContext

  implicit def requestTimeOut : Timeout

  def createOrdererActor : ActorRef

  lazy val orderer = createOrdererActor

  def getProduct(product : ProductOrder) = {
    orderer ! product
  }




}
