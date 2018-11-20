package com.africasTalking.elmer.web
package marshalling

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport

import spray.json.DefaultJsonProtocol

import com.africasTalking.elmer.worker._
import FoodRequestGateway._
import FoodRequestService._


trait WebJsonImplicitsT extends DefaultJsonProtocol with SprayJsonSupport {
  implicit val foodRequestServiceFormat   = jsonFormat2(FoodServiceRequest)
  implicit val foodResponseServiceFormat  = jsonFormat2(FoodServiceResponse)
  implicit val incomingRequestFormat      = jsonFormat2(IncomingFoodServiceRequest)
  implicit val incomingResponseFormat     = jsonFormat2(IncomingFoodServiceResponse)
}
