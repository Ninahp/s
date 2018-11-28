package com.africasTalking.elmer
package worker

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{ Failure, Success }

import akka.actor.{ Actor, ActorLogging, ActorSystem }
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.Unmarshal

import io.atlabs._

import horus.core.http.client.ATHttpClientT

import com.africasTalking.elmer.core.ElmerConfig




object OrderService {


  case class ProductOrder(name: String, quantity: Int)

  // response trait
  sealed trait OrderServiceResponse
  case object Accepted extends OrderServiceResponse
  case object Delivered extends OrderServiceResponse
  case object Failure extends OrderServiceResponse

  case class BrokerResponse(status: String)

}


class OrderService() extends Actor
  with ATHttpClientT
  with ActorLogging
  with ProductMarshaller {

  override implicit val system: ActorSystem = context.system

  def receive = {

    import OrderService._
    case req : ProductOrder =>
      log.info("Processing order")
      val currentSender = sender()

      Marshal(req).to[RequestEntity] onComplete {
        case Failure(ex) => log.error(ex.getMessage)

        case Success(ent) =>
          val request = HttpRequest(
            HttpMethods.POST,
            Uri(ElmerConfig.gateway),
            entity = ent
          )

          log.info("Contacting broker")
          sendHttpRequest(request) onComplete {
            case Failure(ex) =>
              log.error(ex.getMessage)



            case Success(resp) =>
              resp.status.isSuccess() match {

                case false =>
                  currentSender ! OrderService.Failure

                case true =>
                  val brokerResponse = Unmarshal(resp.data).to[BrokerResponse].map {
                    case BrokerResponse("Accepted") => currentSender ! Accepted
                    case BrokerResponse("Delivered") => currentSender ! Delivered
                    case BrokerResponse("Failure") => currentSender ! OrderService.Failure
                  }
              }
          }
      }
  }


}

