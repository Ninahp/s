package com.africasTalking.elmer.core
package util

import spray.json.{ DefaultJsonProtocol, DeserializationException, JsString, JsValue, RootJsonFormat }

import com.africasTalking._

import elmer.core.util.ElmerEnum.{ FoodEnum, FoodOrderStatus }


object ElmerJsonProtocol extends DefaultJsonProtocol {

  implicit object StatusJsonFormat extends RootJsonFormat[FoodOrderStatus.Value] {
    def write(obj: FoodOrderStatus.Value): JsValue = JsString(obj.toString)
    def read(json: JsValue): FoodOrderStatus.Value = json match {
      case JsString(str) => FoodOrderStatus.withName(str)
      case _ => throw new DeserializationException("Enum string expected")
    }
  }

  implicit object FoodEnumJsonFormat extends RootJsonFormat[FoodEnum.Value] {
    def write(obj: FoodEnum.Value): JsValue = JsString(obj.toString)
    def read(json: JsValue): FoodEnum.Value = json match {
      case JsString(str) => FoodEnum.withName(str)
      case _ => throw new DeserializationException("Enum string expected")
    }
  }

}
