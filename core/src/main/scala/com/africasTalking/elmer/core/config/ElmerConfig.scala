package com.africasTalking.elmer.core
package config

import io.atlabs.horus.core._
import config.ATBaseConfigT
import util.ATUtil


object ElmerConfig extends ElmerConfigT

private[config] trait ElmerConfigT extends ATBaseConfigT {
  val webHost        = config.getString("elmer.interface.web.host")
  val webPort        = config.getInt("elmer.interface.web.port")
  val gatewayUrl     = config.getString("elmer.gateway.default-url")

  val defaultTimeout = ATUtil.parseFiniteDuration(config.getString("elmer.http.default-timeout")).get
}
