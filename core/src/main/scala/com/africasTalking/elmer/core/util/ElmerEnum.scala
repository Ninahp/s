package com.africasTalking.elmer.core
package util

object ElmerEnum {

  object OrderRequestStatus extends Enumeration {
    val Accepted          = Value(1)
    val Delivered         = Value(2)
    val Failed            = Value(3)
  }

  object FoodName extends Enumeration {
    val Ugali          	  = Value(1)
    val Rice         	  = Value(2)
    val Egusi             = Value(3)
    val PepperSoup        = Value(4)
    val beefStew		  = Value(5)
    val beefFry			  = Value(6)
    val NaN		  		  = Value(7)
  }
}