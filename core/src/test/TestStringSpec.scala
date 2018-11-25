package com.elmer.config
package config


import util.ATUtil

import org.scalatest


class TestStringSpec {

  val default                       = "Salad"
  val incorrect                     = "mukimo"
  val correctInputIncorrectFormat   = "ManaGu"
  val correctInputCorrectFormat     = "Salad"

  it ("output empy string if food item does not exist") {
    checkStr(incorrect) shouldEqual ""
  }

  it ("output correct format if correct input but wrong format is given") {
    checkStr(correctInputIncorrectFormat) shouldEqual  default
  }

  it ("does not change the input if the default is given") {
    val check = checkStr(correctInputCorrectFormat) == default
    val check1 = correctInputCorrectFormat == default
    check shouldEqual check1
  }
}
