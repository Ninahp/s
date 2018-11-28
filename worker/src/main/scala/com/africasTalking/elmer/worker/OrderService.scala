package com.africasTalking.elmer
package worker

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}
import akka.actor.{Actor, ActorLogging, ActorSystem}
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.Unmarshal
import io.atlabs._
import horus.core.http.client.ATHttpClientT
import com.africasTalking._
import core.{ElmerConfig, _}
import util.ElmerEnum._
import horus.core.snoop.{ATSnoopErrorPublisherT}



object OrderService {

  case class OrderServiceRequest(name: String, quantity: Int)
  case class OrderServiceResponse(status: String)

}


class OrderService() extends Actor
  with ATHttpClientT
  with ProductMarshaller
  with ATSnoopErrorPublisherT{

  override implicit val system: ActorSystem = context.system

  import OrderService._

  def receive = {


    case req : OrderServiceRequest =>
      log.info("Processing order")
      val currentSender = sender()


     val serviceResponse =  for{
        ent <- Marshal(req).to[RequestEntity]
        response <- sendHttpRequest( HttpRequest(
          HttpMethods.POST,
          Uri(ElmerConfig.orderRequestUrl),
          entity = ent
        ))
      } yield response

          serviceResponse onComplete {
            case Failure(ex) =>
              publishError(ex.getMessage)
              currentSender ! Status.Failure

            case Success(resp) =>
              resp.status.isSuccess() match {

                case false =>
                  currentSender ! Status.Failure

                case true =>
                   Unmarshal(resp.data).to[OrderServiceResponse].map {
                    case OrderServiceResponse("Accepted") => currentSender ! Status.Accepted
                    case OrderServiceResponse("Delivered") => currentSender ! Status.Delivered
                    case OrderServiceResponse("Failure") => currentSender ! Status.Failure
                  }
              }
          }
      }


}




