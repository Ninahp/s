package com.africasTalking.elmer.core
package util

import spray.json._

import io.atlabs._

import ElmerEnum._

object ElmerJsonProtocol extends DefaultJsonProtocol {

  implicit object OrderRequestStatusJsonFormat extends RootJsonFormat[OrderRequestStatus.Value] {
    def write(obj: OrderRequestStatus.Value): JsValue = JsString(obj.toString)
    def read(json: JsValue): OrderRequestStatus.Value = json match {
      case JsString(str) => OrderRequestStatus.withName(str)
      case _ => throw new DeserializationException("Enum string expected")
    }
  }
}
