package com.africasTalking.elmer
package worker

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{ Failure, Success }

import akka.actor.{ Actor, ActorSystem }
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.Unmarshal

import io.atlabs._
import horus.core.http.client.ATHttpClientT
import horus.core.snoop.ATSnoopErrorPublisherT

import com.africasTalking._

import elmer.core.ElmerConfig
import elmer.core.util.ElmerEnum._

object OrderService {

  case class EtherOrderServiceRequest(name: String, quantity: Int)
  case class EtherOrderServiceResponse(status: String)

}

class OrderService() extends Actor
  with ATHttpClientT
  with ProductMarshaller
  with ATSnoopErrorPublisherT{

  override implicit val system: ActorSystem = context.system

  import OrderService._

  def receive = {


    case req : EtherOrderServiceRequest =>
      log.info("Processing order")
      val currentSender = sender()

      val serviceResponse =  for{
        ent       <- Marshal(req).to[RequestEntity]
        response  <- sendHttpRequest( HttpRequest(
                                      HttpMethods.POST,
                                      Uri(ElmerConfig.EtherOrderGatewayUrl),
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
                   Unmarshal(resp.data).to[EtherOrderServiceResponse].map {
                    case EtherOrderServiceResponse("Accepted") => currentSender ! Status.Accepted
                    case EtherOrderServiceResponse("Delivered") => currentSender ! Status.Delivered
                    case EtherOrderServiceResponse("Failure") => currentSender ! Status.Failure
                  }
              }
          }
      }


}




