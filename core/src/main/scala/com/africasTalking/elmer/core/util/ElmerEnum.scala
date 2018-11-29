package com.africasTalking.elmer.core
package util

object ElmerEnum {

  object FoodEnum extends Enumeration {
    val Ugali      = Value(0)
    val Rice       = Value(1)
    val beefStew   = Value(2)
    val beefFry    = Value(3)
    val Egusi      = Value(4)
    val PepperSoup = Value(5)
    val Invalid    = Value(6)

    def contains(s:String) = values.exists(_.toString == s)

    def isInvalid(v: Value) = v == Invalid
 }

  object FoodOrderStatus extends Enumeration {
    val Accepted      = Value(200)
    val BadRequest    = Value(400)
    val InternalError = Value(500)
  }

}
