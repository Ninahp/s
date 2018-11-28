package com.africasTalking.elmer.web
package marshalling

import akka.http.scaladsl.marshallers.sprayjson._

import spray.json.DefaultJsonProtocol

import com.africasTalking._

import elmer.worker.FoodRequestService._


trait WebJsonImplicitsT extends DefaultJsonProtocol with SprayJsonSupport {

  implicit val foodServiceRequestFormat   = jsonFormat2(FoodServiceRequest)
  implicit val foodServiceResponseFormat  = jsonFormat2(FoodServiceResponse)
}
