package com.africasTalking.elmer.core
package util

import spray.json._

import io.atlabs._

import horus.core.util.ATJsonProtocol._

import com.africasTalking._

import ElmerEnum._

object ElmerJsonProtocol extends DefaultJsonProtocol {

  implicit object StatusJsonFormat extends RootJsonFormat[Status.Value] {
    def write(obj: Status.Value): JsValue = JsString(obj.toString)
    def read(json: JsValue): Status.Value = json match {
      case JsString(str) => Status.withName(str)
      case _ => throw new DeserializationException("Enum string expected")
    }
  }
}
