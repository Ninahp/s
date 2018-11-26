package com.africasTalking.elmer.worker


import spray.json.DefaultJsonProtocol

import OrderService.{ ProductOrder,BrokerResponse }

trait ProductMarshaller extends DefaultJsonProtocol{

  implicit val order = jsonFormat2(ProductOrder)
  implicit val response = jsonFormat1(BrokerResponse)

}
