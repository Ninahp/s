package com.africasTalking.elmer.core
package util

import spray.json.{ DefaultJsonProtocol, DeserializationException, JsString, JsValue, RootJsonFormat }

import com.africasTalking._

import elmer.core.util.ElmerEnum.{ FoodEnum, Status }


object ElmerJsonProtocol extends DefaultJsonProtocol {

  implicit object StatusJsonFormat extends RootJsonFormat[Status.Value] {
    def write(obj: Status.Value): JsValue = JsString(obj.toString)
    def read(json: JsValue): Status.Value = json match {
      case JsString(str) => Status.withName(str)
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
