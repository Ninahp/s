package com.africasTalking.elmer.productservice

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{ Success, Failure }

import akka.actor.{ Actor, ActorSystem, ActorLogging}
import akka.pattern.ask
import akka.util.Timeout
import akka.stream.ActorMaterializer
import akka.event.Logging
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.util.ByteString


import org.json4s._
import org.json4s.jackson.Serialization
import org.json4s.jackson.Serialization.write

import com.africasTalking._

import elmer.core.util._

import elmer.core.config.ElmerConfig

import elmer.core.query.QueryService._


object BrokerService {
  case class PlaceOrder(order:FoodOrderService)
}

class BrokerService extends Actor
    with ActorLogging
    with ElmerJsonProtocol{

  implicit val system = context.system

  implicit val materializer             = ActorMaterializer()

  override lazy val log                 = Logging(system, classOf[BrokerService])

  val url = ElmerConfig.brokerUrl

  import BrokerService._
  import context.dispatcher

  def receive: Receive = {
    case PlaceOrder(order) => 
      log.info("processing " + PlaceOrder)
      val currentSender = sender
      val requestBody = write(order)
      val request = Http().singleRequest(
        HttpRequest(
          method = HttpMethods.POST,
          uri = Uri(url),
          entity = HttpEntity(ContentTypes.`application/json`, ByteString(requestBody))
        )
      )
      request onComplete {
        case Success(value) =>
          val response = Unmarshal(value.entity).to[FoodOrderServiceResponse]
          response onComplete {
            case Success(response) => currentSender ! response
            case Failure(error)    => 
              currentSender ! FoodOrderServiceResponse(
                status = "The request sent is not valid or the broker is down,try later")
              log.info(s"Error: $error")
          }

        case Failure(error) =>
          log.info(s"$error")
      }
  }
}