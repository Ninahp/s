package com.africasTalking.elmer
package worker

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport

import spray.json.DefaultJsonProtocol


private[worker] case class GatewayRequest(name:String, quantity:Int)
private[worker] case class GatewayResponse(status:String)

private[worker] trait GatewayJsonSupportT extends DefaultJsonProtocol with SprayJsonSupport {
  implicit val gatewayRequestFormat   = jsonFormat2(GatewayRequest)
  implicit val gatewayResponseFormat  = jsonFormat1(GatewayResponse)
}
