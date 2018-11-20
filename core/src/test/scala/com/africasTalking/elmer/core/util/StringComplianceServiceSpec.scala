package com.africasTalking.elmer.core
package util

import org.scalatest.{FlatSpec, Matchers}


class StringComplianceServiceSpec extends FlatSpec
  with Matchers
  with StringComplianceServiceT {

  val rightFormat           = "Ugali"
  val wrongInput            = "spaghetti"
  val rightInputWrongFormat = "uGalI"
  val rightInputRightFormat = "Ugali"

  "String Compliance Function" must "Provide an empty string if the item doesn't exist" in {
    checkString(wrongInput) shouldEqual ""
  }

  it must "Provide the correct Format when the wrong input is formatted wrongly" in {
    checkString(rightInputWrongFormat) shouldEqual  rightFormat
  }

  it must "Not alter the input when given the right format" in {
    val condition1 = checkString(rightInputRightFormat) == rightFormat
    val condition2 = rightInputRightFormat == rightFormat
    condition1 shouldEqual condition2
  }
}
