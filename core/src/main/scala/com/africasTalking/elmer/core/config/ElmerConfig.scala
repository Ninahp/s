package com.africasTalking.elmer.core
package config

import io.atlabs._

import horus.core.config.{ ATBaseConfigT, ATConfig }


object ElmerConfig extends ElmerConfigT

private[config] trait ElmerConfigT extends ATBaseConfigT {
  //Web
  val webHost        = config.getString("elmer.interface.web.host")
  val webPort        = config.getInt("elmer.interface.web.port")

  //Gateway
  val gatewayUrl     = config.getString("elmer.gateway.order-request-url")
}