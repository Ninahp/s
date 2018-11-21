package com.africasTalking.elmer.core
package query

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{ Failure, Success }

import akka.actor.{ Actor, ActorLogging, Props }
import akka.pattern.ask
import akka.util.Timeout
import akka.event.Logging

import com.africasTalking._

import elmer.core.config.ElmerConfig


object QueryService {

  case object FoodFetchQueryServiceRequest

  case class FoodFetchQueryServiceResponse(
    names: List[String]
  )

  case class FoodOrderService(
    quantity: Int,
    name: String
  )

  case class FoodOrderServiceResponse(
    status: String,
  )

  case class Food(
    foodId: Int,
    name:String
  )
}

class QueryService extends Actor
    with ActorLogging{


  implicit val actorSystem              = context.system

  implicit val timeout                  = Timeout(ElmerConfig.queryTimeout)

  override lazy val log                 = Logging(actorSystem, classOf[QueryService])

  import QueryService._

  def receive = {

    case FoodFetchQueryServiceRequest =>
      log.info("processing " + FoodFetchQueryServiceRequest)
      val currentSender = sender
      var foodlist:List[String]       = List("Ugali", "Rice", "beefStew", "beefFry", "Egusi", "PepperSoup")
        currentSender ! foodlist
    }

}
  