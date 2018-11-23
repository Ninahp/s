package com.elmer.core
package util

object ElmerEnum {
  object ResponseStatus extends Enumeration {
    val OK               = Value(1)
    val InvalidParameter = Value(2)
  }

  object ElmerGateway extends Enumeration {
    val Accepted = Value(1)
  }
}
