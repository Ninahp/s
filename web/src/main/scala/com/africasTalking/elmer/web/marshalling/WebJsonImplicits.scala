package com.africasTalking.elmer.web
package marshalling

import akka.http.scaladsl.marshallers.sprayjson._

import spray.json.DefaultJsonProtocol

import com.africasTalking._

import elmer.worker.FoodRequestGateway.FoodGatewayResponse
import elmer.worker.FoodRequestService._
import elmer.core.util.ElmerJsonProtocol._


trait WebJsonImplicitsT extends DefaultJsonProtocol with SprayJsonSupport {

  implicit val foodRequestServiceFormat   = jsonFormat2(FoodServiceRequest)
  implicit val foodResponseServiceFormat  = jsonFormat2(FoodServiceResponse)

  implicit val incomingResponseFormat     = jsonFormat2(FoodGatewayResponse)
}
