package com.africasTalking.elmer.core
package util


trait StringComplianceServiceT {

  //Endpoint were we can retrieve this info
  private val possibleValues = List("Ugali","Rice","beefStew","beefFry","Egusi","PepperSoup")

  def checkString(x:String):String   = {
    val smallList = possibleValues.map(_.toLowerCase)
    smallList.contains(x.toLowerCase) match{
      case true => possibleValues(smallList.indexOf(x.toLowerCase))
      case false => ""
    }
  }
}

