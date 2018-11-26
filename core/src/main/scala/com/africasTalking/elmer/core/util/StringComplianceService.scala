package com.africasTalking.elmer.core
package util


trait StringComplianceServiceT {

  import ElmerEnum._

  private val possibleValues = List(
    FoodEnum.Ugali.toString,
    FoodEnum.beefFry.toString,
    FoodEnum.beefStew.toString,
    FoodEnum.Egusi.toString,
    FoodEnum.PepperSoup.toString,
    FoodEnum.Rice.toString
  )

  def checkString(x:String):String   = {
    val smallList = possibleValues.map(_.toLowerCase)
    smallList.contains(x.toLowerCase) match{
      case true => possibleValues(smallList.indexOf(x.toLowerCase))
      case false => ""
    }
  }
}

