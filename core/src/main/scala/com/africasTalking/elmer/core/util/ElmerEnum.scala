package com.africasTalking.elmer.core.util

object ElmerEnum {


  object Food extends Enumeration {
    val ughali = Value("Ughali")
    val rice = Value("Rice")
    val egusi = Value("Egusi")

    def contains(food : String) = values.exists(_.toString == food)
  }

  object Status extends Enumeration {

    type Status = Value
    val Accepted = Value("Accepted")
    val Failure = Value("Failure")
    val Delivered =  Value ("Delivered")
  }



}
