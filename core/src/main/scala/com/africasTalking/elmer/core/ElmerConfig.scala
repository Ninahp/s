package com.africasTalking.elmer.core

import io.atlabs._

import horus.core.config.ATBaseConfigT

object ElmerConfig extends ElmerConfigTrait

private[core] trait ElmerConfigTrait extends ATBaseConfigT {

  val host = config.getString("elmer.interface.web.host")

  val port = config.getInt("elmer.interface.web.port")

  val EtherOrderGatewayUrl = config.getString("elmer.broker.order-request-url")

}

