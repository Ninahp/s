package com.elmer.core
package Foodconfig

import io.atlabs.horus.core._
import config.ATBaseConfigT
import util.ATUtil


object FoodConfig extends FoodConfigT {

  private trait FoodConfigT extends ATBaseConfigT {
    val webHost     = config.getString("elmer.interface.web.host")
    val gatewayUrl  = config.getString("elmer.gateway.default-url")
    val webPort     = config.getInt("elmer.interface.web.port")


    val defaultTimeout = ATUtil.parseFiniteDuration(config.getString("elmer.http.default-timeout")).get
  }

}
