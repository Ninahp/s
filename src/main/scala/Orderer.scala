import Orderer.ProductOrder
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

class Orderer(implicit mSystem : ActorSystem) extends Actor with Request with ActorLogging with ProductMarshaller {


  override def system: ActorSystem = mSystem
  implicit val executionContext = system.dispatcher

  def receive = {
    case  p @ ProductOrder(name, qty) =>
      Marshal(p).to[RequestEntity] onComplete{
        case Success(ent ) =>
            val request = HttpRequest(
              HttpMethods.POST,
              Uri("https://ether.at-labs.at-internal.com/order/request"),
              entity = ent
            )
          makeRequest(request) onComplete{
            case Success(resp) => println(resp)
            case Failure(ex) => log.error(ex.getMessage)

          }



        case Failure(ex) => log.error(ex.getMessage)
      }






  }
}



object Orderer {




  sealed trait Order
  case class ProductOrder(name : String, quantity : Int) extends Order

  sealed trait Response
  case object Accepted
  case object Delivered
  case object Failure






}


trait Request{
 implicit def system : ActorSystem

  def makeRequest(httpRequest: HttpRequest) : Future[HttpResponse] = {
     Http().singleRequest(httpRequest)
  }
}
