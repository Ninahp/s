package com.africasTalking.elmer
package worker.gateway

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport

import spray.json.DefaultJsonProtocol

import com.africasTalking._

import elmer.core.util.ElmerJsonProtocol._
import elmer.core.util.ElmerEnum._


private[worker] case class etherGatewayRequest(name:FoodEnum.Value, quantity:Int)
private[worker] case class etherGatewayResponse(status:FoodOrderStatus.Value)

private[worker] trait GatewayJsonSupportT extends DefaultJsonProtocol with SprayJsonSupport {
  implicit val gatewayRequestFormat       = jsonFormat2(etherGatewayRequest)
  implicit val gatewayResponseFormat      = jsonFormat1(etherGatewayResponse)
}
