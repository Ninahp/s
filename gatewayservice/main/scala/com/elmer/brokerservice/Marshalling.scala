package com.elmer
package BrokerService

import spray.json.DefaultJsonProtocol

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport


private case class GatewayRequest(name:String, quantity:Int)
private case class GatewayResponse(status:String)

private trait GatewayJsonSupportT extends DefaultJsonProtocol with SprayJsonSupport {
  implicit val gatewayRequestFormat   = jsonFormat2(GatewayRequest)
  implicit val gatewayResponseFormat  = jsonFormat1(GatewayResponse)
}
