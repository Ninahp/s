package com.africasTalking.elmer.food
package gateway.marshalling

import akka.http.scaladsl.marshallers.sprayjson._

import spray.json._

import io.atlabs._

import horus.core.util.ATJsonProtocol

import com.africasTalking._

import elmer.core.util._

import elmer.food.gateway._

trait ElmerJsonSupportT extends SprayJsonSupport with DefaultJsonProtocol {

  import ATJsonProtocol._
  import FoodOrderGateway._
  import ElmerJsonProtocol._

  implicit val EtherOrderRequestFormat   = jsonFormat2(EtherOrderRequest)
  implicit val EtherResponseFormat       = jsonFormat1(EtherResponse)

}
