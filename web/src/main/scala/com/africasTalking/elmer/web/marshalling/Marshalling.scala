package com.africasTalking.elmer.web
package marshalling

import akka.http.scaladsl.marshallers.sprayjson._

import spray.json._

import io.atlabs._

import horus.core.util.ATJsonProtocol

import com.africasTalking._

import elmer.core.query._

import elmer.food._

trait ElmerJsonSupportT extends SprayJsonSupport with DefaultJsonProtocol {

  import ATJsonProtocol._
  import QueryService._
  import FoodOrderService._

  implicit val FoodFetchQueryServiceResponseFormat  = jsonFormat1(FoodFetchQueryServiceResponse)
  implicit val FoodOrderServiceResponseFormat       = jsonFormat1(FoodOrderServiceResponse)
  implicit val FoodOrderServiceRequestFormat        = jsonFormat2(FoodOrderServiceRequest)

  def write(foodorder: FoodOrderServiceRequest): JsValue = {
    JsObject(
      "name"     -> foodorder.name.toJson,
      "quantity" -> foodorder.quantity.toJson
    )
  }
}