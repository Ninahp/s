package com.africasTalking.elmer.core.actors

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

import akka.actor.{Actor, ActorLogging, ActorSystem}
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.stream.ActorMaterializer

import com.africasTalking.elmer.core.Config.ElmerConfig
import com.africasTalking.elmer.core.actors.Orderer._
import com.africasTalking.elmer.core.marshalling.ProductMarshaller




class Orderer(implicit val system: ActorSystem) extends Actor
  with HttpClient
  with ActorLogging
  with ProductMarshaller {

  def receive = {

    case p @ ProductOrder(_,_) =>
      log.info("Processing order")
      val orig_sender = sender()
      Marshal(p).to[RequestEntity] onComplete {
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
                  orig_sender ! Orderer.Failure

                case true =>
                  val rsp = resp.data.split("\"")
                    rsp(3) match {
                      case "Accepted"    => orig_sender ! Accepted
                      case "Delivered"   => orig_sender ! Delivered
                      case "Failure"     => orig_sender ! Orderer.Failure
                      case other: String => log.info(other)
                    }
              }
          }
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

trait HttpClient {

  implicit val system: ActorSystem
  implicit val materializer = ActorMaterializer()

  def sendHttpRequest(request: HttpRequest) = {

    for {
      resp <- Http().singleRequest(request)
      data <- Unmarshal(resp).to[String]
    } yield ProductResponse(resp.status,data)
  }
}

