package com.africasTalking.elmer.core.marshalling

import spray.json.DefaultJsonProtocol

trait ProductMarshaller extends DefaultJsonProtocol{
  import com.africasTalking.elmer.core.actors.Orderer._
  implicit val order = jsonFormat2(ProductOrder)

}
