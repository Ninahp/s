package com.africasTalking.elmer.web
package marshalling

import akka.http.scaladsl.marshallers.sprayjson._

import spray.json._

import io.atlabs._

import horus.core.util.ATJsonProtocol

import com.africasTalking._

import elmer.core.util._

import elmer.food._

trait ElmerJsonSupportT extends SprayJsonSupport with DefaultJsonProtocol {

  import ATJsonProtocol._
  import FoodOrderService._
  import ElmerJsonProtocol._

  implicit val FoodOrderGatewayResponseFormat       = jsonFormat2(FoodOrderServiceResponse)
  implicit val FoodOrderGatewayRequestFormat        = jsonFormat2(FoodOrderServiceRequest)

}