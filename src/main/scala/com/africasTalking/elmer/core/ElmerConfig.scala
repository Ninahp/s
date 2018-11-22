package com.africasTalking.elmer.core

import com.typesafe.config.{Config, ConfigFactory}
import io.atlabs.horus.core.config.ATBaseConfigT

trait ElmerConfigTrait extends ATBaseConfigT{
  //override val config: Config = ConfigFactory.load("application.conf")

  val host = config.getString("elmer.interface.web.host")
  val port = config.getInt("elmer.interface.web.port")
  val gateway = config.getString("elmer.default-gateway")

}

object ElmerConfig extends ElmerConfigTrait
