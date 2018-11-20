package com.africasTalking.elmer.core
package util

object ElmerEnum {
  object ResponseStatus extends Enumeration {
    val OK               = Value(1)
    val InvalidParameter = Value(2)
  }

  object GatewayResponse extends Enumeration {
    val Accepted = Value(1)
  }
}
