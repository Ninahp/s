package com.africasTalking.elmer.worker


import spray.json.{ DefaultJsonProtocol }
import OrderService.ProductOrder

trait ProductMarshaller extends DefaultJsonProtocol{

  implicit val order = jsonFormat2(ProductOrder)

}
