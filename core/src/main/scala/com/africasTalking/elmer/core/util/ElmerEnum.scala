package com.africasTalking.elmer.core
package util

object ElmerEnum {

  object FoodEnum extends Enumeration {
    val Ugali      = Value(1)
    val Rice       = Value(2)
    val beefStew   = Value(3)
    val beefFry    = Value(4)
    val Egusi      = Value(5)
    val PepperSoup = Value(6)

    def contains(s:String) = values.exists(_.toString == s)
 }

  object FoodOrderStatus extends Enumeration {
    val Accepted      = Value(200)
    val BadRequest    = Value(400)
    val InternalError = Value(500)
  }

}
