package com.africasTalking.elmer.worker

import spray.json.DefaultJsonProtocol

import OrderService.{ EtherOrderServiceRequest, EtherOrderServiceResponse}



trait ProductMarshaller extends DefaultJsonProtocol{

  implicit val order = jsonFormat2(EtherOrderServiceRequest)
  implicit val response = jsonFormat1(EtherOrderServiceResponse)

}
