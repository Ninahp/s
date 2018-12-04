package com.africasTalking.elmer.core
package util

object ElmerEnum {

  object OrderRequestStatus extends Enumeration {
    val Accepted          = Value(1)
    val Delivered         = Value(2)
    val Failure           = Value(3)
  }
}