package com.elmer.core
package util


trait TestStringT {

  private val foodItems = List("Managu","Mukimo","Matumbofry","Chapati","Pilau","Salad")

  def checkStr(f:String):String = {

    val foods = foodItems.map(_.toLowerCase)
    }
  }