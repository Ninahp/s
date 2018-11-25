package com.git elmer.core
package util


trait TestStringT {

  private val foodItems = List("Managu","Mukimo","MatumboFry","Chapati","Pilau","Salad")

  def checkStr(f:String):String = {

    val foods = foodItems.map(_.toLowerCase)
    }
  }