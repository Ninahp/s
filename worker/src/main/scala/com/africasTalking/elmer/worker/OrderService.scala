package com.africasTalking.elmer
package worker

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.actor.{Actor, ActorLogging, ActorSystem}
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model._
import io.atlabs._
import horus.core.http.client.ATHttpClientT
import com.africasTalking.elmer.core.ElmerConfig
import OrderService._
import spray.json.DefaultJsonProtocol._










object OrderService {

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


class OrderService() extends Actor
  with ATHttpClientT
  with ActorLogging
  with ProductMarshaller {

  override implicit val system: ActorSystem = context.system



  def receive = {

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
                  val rsp = resp.data.split("\"")
                  rsp(3) match {
                    case "Accepted"    => currentSender ! Accepted
                    case "Delivered"   => currentSender ! Delivered
                    case "Failure"     => currentSender ! OrderService.Failure
                    case other: String => log.info(other)
                  }
              }
          }
      }
  }


}

