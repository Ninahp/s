package com.africasTalking.elmer.core
package config

import collection.JavaConversions._
import com.typesafe.config.ConfigFactory

import io.atlabs._

import horus.core.config.ATBaseConfigT
import horus.core.util.ATUtil

object ElmerConfig extends ElmerConfigT

private[config] trait ElmerConfigT extends ATBaseConfigT {
	
  // Broker
  val brokerUrl           = config.getString("elmer.broker.url")
  
  // API
  val apiInterface        = config.getString("elmer.interface.web.host")
  val apiPort             = config.getInt("elmer.interface.web.port")
  
}
