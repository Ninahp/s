package com.africasTalking.elmer.core
package config

import collection.JavaConversions._
import com.typesafe.config.ConfigFactory

import io.atlabs._

import horus.core.util.ATUtil

import horus.core.config.ATBaseConfigT

import com.africasTalking._


object ElmerConfig extends ElmerConfig

private[config] trait ElmerConfig {

  val config = ConfigFactory.load
  config.checkValid(ConfigFactory.defaultReference)


  // Broker
  val brokerUrl           = config.getString("elmer.broker.url")
  
  // API
  val apiInterface        = config.getString("elmer.interface.web.host")
  val apiPort             = config.getInt("elmer.interface.web.port")

}
