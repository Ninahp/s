package com.africasTalking.elmer.core
package query

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{ Failure, Success }

import akka.actor.{ Actor, ActorLogging, Props }
import akka.pattern.ask
import akka.util.Timeout
import akka.event.Logging

import io.atlabs._

import horus.core.config.ATConfig

import horus.core.util.{ ATCash, ATCCPrinter, ATJsonProtocol, ATUtil }

import com.africasTalking._

import elmer.core.config.ElmerConfig


object QueryService {

  case object FoodFetchQueryServiceRequest

  case class FoodFetchQueryServiceResponse(
    names: List[String]
  )extends ATCCPrinter

}

class QueryService extends Actor
    with ActorLogging{

  implicit val actorSystem              = context.system

  implicit val timeout                  = Timeout(ATConfig.httpRequestTimeout)

  import QueryService._

  def receive = {

    case FoodFetchQueryServiceRequest =>
      log.info("processing " + FoodFetchQueryServiceRequest)
      val currentSender = sender
      var foodlist:List[String]       = List("Ugali", "Rice", "beefStew", "beefFry", "Egusi", "PepperSoup")
        currentSender ! foodlist
    }

}
  