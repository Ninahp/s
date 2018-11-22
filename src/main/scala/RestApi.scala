
import scala.concurrent.ExecutionContext

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.util.Timeout
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.pattern.ask



class RestApi(implicit  system : ActorSystem , timeout: Timeout) extends Routes{
  override implicit def executionContext: ExecutionContext = system.dispatcher

  override implicit def requestTimeOut: Timeout = timeout

  override def createOrdererActor: ActorRef = system.actorOf(Props(new Orderer()),"orderer")

}


trait Routes extends ProductApi with ProductMarshaller {
  import Orderer._

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

import Orderer._
  implicit def executionContext : ExecutionContext

  implicit def requestTimeOut : Timeout

  def createOrdererActor : ActorRef

  lazy val orderer = createOrdererActor

  def getProduct(product : ProductOrder) = {
    (orderer ? product).mapTo[Response]
  }
}
