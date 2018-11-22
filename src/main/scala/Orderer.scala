import Orderer.{Accepted, BrokerResponse, Delivered, ProductOrder}
import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model._
import spray.json._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json.DefaultJsonProtocol._

import scala.concurrent.Future
import scala.util.{Failure, Success}

class Orderer(implicit mSystem: ActorSystem) extends Actor
  with Request
  with ActorLogging
  with ProductMarshaller {


  override def system: ActorSystem = mSystem

  implicit val executionContext = system.dispatcher

  def receive = {

    case p @ ProductOrder(_,_) =>
      log.info("Processing order")
      val orig_sender = sender()
      Marshal(p).to[RequestEntity] onComplete {
        case Success(ent) =>
          val request = HttpRequest(
            HttpMethods.POST,
            Uri("https://ether.at-labs.at-internal.com/order/request"),
            entity = ent
          )
          log.info("Contacting broker")
          makeRequest(request) onComplete {
            case Success(resp) =>
              val rsp = resp.entity.toString.split("\"")
              try {
                rsp(3) match {
                  case "Accepted" => orig_sender ! Accepted
                  case "Delivered" => orig_sender ! Delivered
                  case "Failure" => orig_sender ! Orderer.Failure
                  case other: String => log.info(other)
                }
              } catch {
                case _: ArrayIndexOutOfBoundsException => orig_sender ! Orderer.Failure
              }
            case Failure(ex) => log.error(ex.getMessage)
          }

        case Failure(ex) => log.error(ex.getMessage)
      }
  }
}


object Orderer {

  //request trait
  sealed trait Order

  case class ProductOrder(name: String, quantity: Int) extends Order

  // response trait
  sealed trait Response

  case object Accepted extends Response

  case object Delivered extends Response

  case object Failure extends Response

  case class BrokerResponse(Status: String)


}


trait Request {
  implicit def system: ActorSystem
  def makeRequest(httpRequest: HttpRequest): Future[HttpResponse] = {
    Http().singleRequest(httpRequest)
  }
}
