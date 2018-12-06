package com.africasTalking.elmer.food
package gateway.marshalling

import akka.http.scaladsl.marshallers.sprayjson._

import spray.json._

import io.atlabs._

import horus.core.util.ATJsonProtocol
import horus.core.util.ATCCPrinter

import com.africasTalking._

import elmer.core.util._

import elmer.core.util.ElmerEnum.{ FoodName, OrderRequestStatus }

import elmer.food.gateway._

private[gateway] case class EtherOrderRequest(
    name: FoodName.Value,
    quantity: Int
  )extends ATCCPrinter

private[gateway] case class EtherOrderResponse(
    status: OrderRequestStatus.Value
  )extends ATCCPrinter

private[gateway] trait ElmerJsonSupportT extends SprayJsonSupport with DefaultJsonProtocol {

  import ATJsonProtocol._
  import FoodOrderGateway._
  import ElmerJsonProtocol._

  implicit val EtherOrderRequestFormat   = jsonFormat2(EtherOrderRequest)
  implicit val EtherResponseFormat       = jsonFormat1(EtherOrderResponse)

}
