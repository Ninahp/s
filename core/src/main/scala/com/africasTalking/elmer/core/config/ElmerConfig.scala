package com.africasTalking.elmer.core
package config

import com.typesafe.config.ConfigFactory

import io.atlabs._

import horus.core.config.ATBaseConfigT
import horus.core.util.ATUtil

object ElmerConfig extends ElmerConfigT

private[config] trait ElmerConfigT extends ATBaseConfigT {

  // Broker
  val brokerOrderRequestUrl = config.getString("elmer.broker.order-request-url")
  
  // API
  val webHost        		= config.getString("elmer.interface.web.host")
  val WebPort        		= config.getInt("elmer.interface.web.port")
  
}
