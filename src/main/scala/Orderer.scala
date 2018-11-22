
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

import akka.actor.{Actor, ActorLogging, ActorSystem, Props, Status}
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model._
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer




import Orderer._

class Orderer(implicit val system: ActorSystem) extends Actor
  with HttpClient
  with ActorLogging
  with ProductMarshaller {

  def receive = {

    case p @ ProductOrder(_,_) =>
      log.info("Processing order")
      val orig_sender = sender()
      println("hgfdfghjhkjhkgjcfhgfcgvhj")
      Marshal(p).to[RequestEntity] onComplete {
        case Success(ent) =>
          val request = HttpRequest(
            HttpMethods.POST,
            Uri("https://ether.at-labs.at-internal.com/order/request"),
            entity = ent
          )
          log.info("Contacting broker")
          sendHttpRequest(request) onComplete {
            case Success(resp) =>
              resp.status.isSuccess() match {
                case true =>
                  val rsp = resp.data.split("\"")

                    rsp(3) match {
                      case "Accepted"    => orig_sender ! Accepted
                      case "Delivered"   => orig_sender ! Delivered
                      case "Failure"     => orig_sender ! Orderer.Failure
                      case other: String => log.info(other)
                    }


                case false =>
                  orig_sender ! Orderer.Failure
              }

            case Failure(ex) =>
              log.error(ex.getMessage)
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

  case class ProductResponse(status : StatusCode, data : String)


}

case class ATHttpClientResponse(
  status: StatusCode,
  data: String
)



trait HttpClient {

  implicit val system: ActorSystem
  implicit val materializer = ActorMaterializer()

  def sendHttpRequest(request: HttpRequest) = {

    for {
      resp <- Http().singleRequest(request)
      data <- Unmarshal(resp).to[String]
    }yield ProductResponse(resp.status,data)
  }
}

