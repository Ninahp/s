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

  
  // Actor-Timeout
  val mysqlTimeout        = ATUtil.parseFiniteDuration(config.getString("elmer.actor-timeout.query")).get
  val queryTimeout        = ATUtil.parseFiniteDuration(config.getString("elmer.actor-timeout.broker")).get

  // Broker
  val brokerUrl           = config.getString("elmer.broker.url")
  
  //http
  val httpRequestTimeout  = ATUtil.parseFiniteDuration(config.getString("elmer.http.request-timeout")).get

  // API
  val apiInterface        = config.getString("elmer.interface.web.host")
  val apiPort             = config.getInt("elmer.interface.web.port")

}
